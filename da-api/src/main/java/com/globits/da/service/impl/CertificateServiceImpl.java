package com.globits.da.service.impl;

import com.globits.da.Exception.ErrorCode;
import com.globits.da.Mapper.CertificateMapper;
import com.globits.da.domain.entity.Certificate;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.CertificateRequestDto;
import com.globits.da.dto.response.CertificateResponseDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateMapper certificateMapper;
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public CertificateResponseDto save(CertificateRequestDto dto) {
        Certificate certificate = certificateMapper.toEntity(dto);
        if (dto.getProvinceId() != null) {
            Province province = provinceRepository.findById(dto.getProvinceId())
                    .orElseThrow(() -> new RuntimeException("Province not found with id: " + dto.getProvinceId()));
            certificate.setProvince(province);
        }
        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    @Override
    public CertificateResponseDto update(Long id, CertificateRequestDto dto) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Not found certificate with id "+id));

         certificateMapper.updateEntityFromDto(dto,certificate);

        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    @Override
    public void delete(Long id) {
        Certificate certificate= certificateRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Not found certificate with id "+id));
        certificateRepository.delete(certificate);
    }

    @Override
    public Page<CertificateResponseDto> search(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public CertificateResponseDto getById(Long id) {
        Certificate certificate=certificateRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Not found certificate with id "+id));
        return certificateMapper.toDto(certificate);
    }

    @Override
    public List<CertificateResponseDto> getAllCertificates() {
        List<Certificate> certificate=certificateRepository.findAll();
        return certificate.stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());
    }
}
