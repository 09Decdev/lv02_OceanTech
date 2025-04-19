package com.globits.da.service;

import com.globits.da.dto.request.CertificateTypeRequestDto;
import com.globits.da.dto.response.CertificateTypeResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface CertificateTypeService {
    CertificateTypeResponseDto getCertificateTypeById(Long id);

    CertificateTypeResponseDto createCertificateType(CertificateTypeRequestDto certificateTypeRequestDto);

    CertificateTypeResponseDto updateCertificateType(Long id, CertificateTypeRequestDto certificateTypeRequestDto);

    boolean deleteCertificateType(Long id);
}
