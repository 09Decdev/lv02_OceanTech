package com.globits.da.mapper;

import com.globits.da.domain.entity.Certificate;
import com.globits.da.domain.entity.CertificateType;
import com.globits.da.dto.request.CertificateRequestDto;
import com.globits.da.dto.request.CertificateTypeRequestDto;
import com.globits.da.dto.response.CertificateTypeResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CertificateTypeMapper {

    public CertificateType toEntity(CertificateTypeRequestDto certificateTypeRequestDto) {
        CertificateType certificateType = new CertificateType();
        certificateType.setId(certificateTypeRequestDto.getId());
        certificateType.setType(certificateTypeRequestDto.getType());
        certificateType.setDescription(certificateTypeRequestDto.getDescription());

        return certificateType;
    }
    public CertificateTypeResponseDto toDto(CertificateType certificateType) {
        CertificateTypeResponseDto responseDto=new CertificateTypeResponseDto();
        responseDto.setId(certificateType.getId());
        responseDto.setType(certificateType.getType());
        responseDto.setDescription(certificateType.getDescription());
        return responseDto;
    }
    public void updateEntityFromDto(CertificateTypeRequestDto dto, CertificateType certificateType) {
        if (dto != null) {
           certificateType.setDescription(dto.getDescription());
           certificateType.setType(dto.getType());

        }
    }
}
