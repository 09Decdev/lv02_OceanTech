package com.globits.da.service;

import com.globits.da.dto.request.CertificateRequestDto;
import com.globits.da.dto.response.CertificateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public interface CertificateService {
    CertificateResponseDto save(CertificateRequestDto dto);
    CertificateResponseDto update(Long id, CertificateRequestDto dto);
    void delete(Long id);
    Page<CertificateResponseDto> search(String keyword, Pageable pageable);
    CertificateResponseDto getById(Long id);
    List<CertificateResponseDto> getAllCertificates();
}
