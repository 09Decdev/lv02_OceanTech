package com.globits.da.rest;

import com.globits.da.dto.request.CertificateTypeRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.CertificateTypeResponseDto;
import com.globits.da.service.CertificateTypeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/type")
public class RestCertificateTypeController {
    private final CertificateTypeService certificateTypeService;

    public RestCertificateTypeController(CertificateTypeService certificateTypeService) {
        this.certificateTypeService = certificateTypeService;
    }

    @PostMapping("/create")
    public ApiResponse<CertificateTypeResponseDto> createCertificateType(
            @Valid @RequestBody CertificateTypeRequestDto certificateTypeRequestDto) {
        ApiResponse<CertificateTypeResponseDto>result=new ApiResponse<>();
        result.setData(certificateTypeService.createCertificateType(certificateTypeRequestDto));

        return result;
    }

    @GetMapping("/{id}")
    public ApiResponse<CertificateTypeResponseDto> getCertificateType(@PathVariable Long id) {
        ApiResponse<CertificateTypeResponseDto>result=new ApiResponse<>();
        result.setData(certificateTypeService.getCertificateTypeById(id));

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<CertificateTypeResponseDto> updateCertificateType(@PathVariable Long id,
                                                                         @RequestBody @Valid CertificateTypeRequestDto certificateTypeRequestDto) {
        ApiResponse<CertificateTypeResponseDto>result=new ApiResponse<>();
        result.setData(certificateTypeService.updateCertificateType(id, certificateTypeRequestDto));
        return result;
    }
}
