package com.globits.da.service.impl;

import com.globits.da.domain.entity.CertificateType;
import com.globits.da.dto.request.CertificateTypeRequestDto;
import com.globits.da.dto.response.CertificateTypeResponseDto;
import com.globits.da.exception.DuplicateEntryException;
import com.globits.da.exception.ErrorCode;
import com.globits.da.mapper.CertificateTypeMapper;
import com.globits.da.repository.CertificateTypeRepository;
import com.globits.da.service.CertificateTypeService;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeServerImpl implements CertificateTypeService {
    private final CertificateTypeRepository certificateTypeRepository;
    private final CertificateTypeMapper certificateTypeMapper;

    public CertificateTypeServerImpl(CertificateTypeRepository certificateTypeRepository, CertificateTypeMapper certificateTypeMapper) {
        this.certificateTypeRepository = certificateTypeRepository;
        this.certificateTypeMapper = certificateTypeMapper;
    }

    @Override
    public CertificateTypeResponseDto getCertificateTypeById(Long id) {
        CertificateType certificateType = certificateTypeRepository.findById(id)
                .orElseThrow(() -> new DuplicateEntryException(ErrorCode.NOT_FOUND_TYPE.getMessage() + id));
        return certificateTypeMapper.toDto(certificateType);
    }

    @Override
    public CertificateTypeResponseDto createCertificateType(CertificateTypeRequestDto certificateTypeRequestDto) {
        CertificateType certificateType = certificateTypeMapper.toEntity(certificateTypeRequestDto);
        certificateTypeRepository.save(certificateType);
        return certificateTypeMapper.toDto(certificateType);
    }

    @Override
    public CertificateTypeResponseDto updateCertificateType(Long id, CertificateTypeRequestDto certificateTypeRequestDto) {
        CertificateType certificateType = certificateTypeRepository.findById(id).orElseThrow(() -> new DuplicateEntryException(ErrorCode.NOT_FOUND_TYPE.getMessage() + id));
        certificateTypeMapper.updateEntityFromDto(certificateTypeRequestDto, certificateType);
        certificateTypeRepository.save(certificateType);
        return certificateTypeMapper.toDto(certificateType);
    }

    @Override
    public boolean deleteCertificateType(Long id) {
        CertificateType certificateType = certificateTypeRepository.findById(id)
                .orElseThrow(() -> new DuplicateEntryException(ErrorCode.NOT_FOUND_TYPE.getMessage() + id));
        certificateTypeRepository.deleteById(certificateType.getId());
        return true;
    }
}
