package com.globits.da.rest;

import com.globits.da.dto.request.CertificateRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.CertificateResponseDto;
import com.globits.da.service.impl.CertificateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/certificate")
@Validated
public class RestCertificateController {
    @Autowired
    private CertificateServiceImpl certificateService;

    @GetMapping("/{id}")
    public ApiResponse<CertificateResponseDto> getCertificate(@PathVariable Long id) {
        ApiResponse<CertificateResponseDto> result = new ApiResponse<>();
        result.setData(certificateService.getById(id));

        return result;
    }

    @GetMapping
    public ApiResponse<List<CertificateResponseDto>> getAllCertificates() {
        ApiResponse<List<CertificateResponseDto>> result = new ApiResponse<>();
        result.setData(certificateService.getAllCertificates());
        return result;
    }

    @PostMapping("/create")
    public ApiResponse<CertificateResponseDto> createCertificate(@Valid @RequestBody CertificateRequestDto certificate) {
        ApiResponse<CertificateResponseDto> result = new ApiResponse<>();
        result.setData(certificateService.save(certificate));
        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<CertificateResponseDto> updateCertificate(@PathVariable Long id,
                                                                 @Valid @RequestBody CertificateRequestDto certificate) {
        ApiResponse<CertificateResponseDto> result = new ApiResponse<>();
        result.setData(certificateService.update(id, certificate));
        return result;

    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCertificate(@PathVariable Long id) {
        ApiResponse<String> result = new ApiResponse<>();
        certificateService.delete(id);
        result.setMessage("Certificate deleted");
        return result;
    }


}
