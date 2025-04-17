package com.globits.da.service.impl;

import com.globits.da.exception.CertificateNotFoundException;
import com.globits.da.exception.CertificateNotValidsException;
import com.globits.da.mapper.CertificateMapper;
import com.globits.da.domain.entity.Certificate;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.CertificateRequestDto;
import com.globits.da.dto.response.CertificateResponseDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.CertificateService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    private final CertificateMapper certificateMapper;

    private final ProvinceRepository provinceRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository, CertificateMapper certificateMapper, ProvinceRepository provinceRepository) {
        this.certificateRepository = certificateRepository;
        this.certificateMapper = certificateMapper;
        this.provinceRepository = provinceRepository;
    }

    private boolean validateCertificateDates(LocalDate issueDate, LocalDate expiryDate) {
        if (issueDate == null || expiryDate == null) {
            return true;
        }
        return !issueDate.isAfter(expiryDate);
    }

    @Override
    public CertificateResponseDto save(CertificateRequestDto dto) {
        if (!validateCertificateDates(dto.getIssueDate(), dto.getExpiryDate())) {
            throw new CertificateNotValidsException();
        }

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

        if (!validateCertificateDates(dto.getIssueDate(), dto.getExpiryDate())) {
            throw new CertificateNotValidsException();
        }

        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException(id));

        certificateMapper.updateEntityFromDto(dto, certificate);

        return certificateMapper.toDto(certificateRepository.save(certificate));
    }

    @Override
    public void delete(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found certificate with id " + id));
        certificateRepository.delete(certificate);
    }

    @Override
    public Page<CertificateResponseDto> search(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public CertificateResponseDto getById(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found certificate with id " + id));
        return certificateMapper.toDto(certificate);
    }

    @Override
    public List<CertificateResponseDto> getAllCertificates() {
        List<Certificate> certificates = certificateRepository.findAll();
        return certificates.stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());
    }
}
