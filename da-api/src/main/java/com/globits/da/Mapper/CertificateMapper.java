package com.globits.da.Mapper;

import com.globits.da.domain.entity.Certificate;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.CertificateRequestDto;
import com.globits.da.dto.response.CertificateResponseDto;
import com.globits.da.dto.response.ProvinceResponseDto;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateMapper {

    private final ProvinceRepository provinceRepository;

    public CertificateMapper(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    // Chuyển đổi từ CertificateRequestDto sang Certificate entity
    public Certificate toEntity(CertificateRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Certificate certificate = new Certificate();
        certificate.setName(dto.getName());
        certificate.setIssuingOrganization(dto.getIssuingOrganization());
        certificate.setIssueDate(dto.getIssueDate());
        certificate.setExpiryDate(dto.getExpiryDate());

        // Kiểm tra ProvinceId từ dto và tìm Province trong DB
        if (dto.getProvinceId() != null) {
            provinceRepository.findById(dto.getProvinceId()).ifPresent(certificate::setProvince);
        }

        return certificate;
    }

    // Chuyển đổi từ Certificate entity sang CertificateResponseDto
    public CertificateResponseDto toDto(Certificate certificate) {
        if (certificate == null) {
            return null;
        }

        CertificateResponseDto dto = new CertificateResponseDto();
        dto.setId(certificate.getId());
        dto.setName(certificate.getName());
        dto.setIssuingOrganization(certificate.getIssuingOrganization());
        dto.setIssueDate(certificate.getIssueDate());
        dto.setExpiryDate(certificate.getExpiryDate());

        // Nếu certificate có Province, chuyển đổi Province thành DTO và thêm vào response DTO
        if (certificate.getProvince() != null) {
            Province province = certificate.getProvince();
            ProvinceResponseDto provinceDto = new ProvinceResponseDto();
            provinceDto.setId(province.getId());
            provinceDto.setName(province.getName());
            provinceDto.setCode(province.getCode());
            dto.setProvinceDto(provinceDto);
        }

        return dto;
    }

    // Chuyển đổi danh sách Certificate entities sang danh sách CertificateResponseDtos
    public List<CertificateResponseDto> toDtoList(List<Certificate> certificates) {
        if (certificates == null) {
            return new ArrayList<>();
        }

        List<CertificateResponseDto> result = new ArrayList<>();
        for (Certificate certificate : certificates) {
            if (certificate != null) {
                CertificateResponseDto dto = toDto(certificate);
                if (dto != null) {
                    result.add(dto);
                }
            }
        }

        return result;
    }

    // Cập nhật entity Certificate từ CertificateRequestDto
    public void updateEntityFromDto(CertificateRequestDto dto, Certificate certificate) {
        if (dto != null) {
            certificate.setName(dto.getName());
            certificate.setIssuingOrganization(dto.getIssuingOrganization());
            certificate.setIssueDate(dto.getIssueDate());
            certificate.setExpiryDate(dto.getExpiryDate());

            // Kiểm tra ProvinceId và cập nhật province nếu cần
            if (dto.getProvinceId() != null) {
                provinceRepository.findById(dto.getProvinceId()).ifPresent(certificate::setProvince);
            }
        }
    }
}
