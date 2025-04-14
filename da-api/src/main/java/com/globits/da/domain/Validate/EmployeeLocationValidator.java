package com.globits.da.domain.Validate;

import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EmployeeLocationValidator implements ConstraintValidator<ValidEmployeeLocation, EmployeeRequestDto> {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public boolean isValid(EmployeeRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        boolean hasProvince = dto.getProvinceId() != null;
        boolean hasDistrict = dto.getDistrictId() != null;
        boolean hasCommune = dto.getCommuneId() != null;

        boolean allProvided = hasProvince && hasDistrict && hasCommune;
        boolean allEmpty = !hasProvince && !hasDistrict && !hasCommune;

        if (!allProvided && !allEmpty) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Phải cung cấp đầy đủ Tỉnh, Huyện, Xã hoặc để trống cả 3")
                    .addPropertyNode("provinceId")
                    .addConstraintViolation();
            return false;
        }

        if (allEmpty) {
            return true;
        }

        // Kiểm tra huyện
        Optional<District> optionalDistrict = districtRepository.findById(dto.getDistrictId());
        if (!optionalDistrict.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Không tìm thấy Huyện với id: " + dto.getDistrictId())
                    .addPropertyNode("districtId")
                    .addConstraintViolation();
            return false;
        }
        District district = optionalDistrict.get();

        if (district.getProvince() == null || !district.getProvince().getId().equals(dto.getProvinceId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Huyện không thuộc Tỉnh được chỉ định")
                    .addPropertyNode("districtId")
                    .addConstraintViolation();
            return false;
        }

        // Kiểm tra xã
        Optional<Commune> optionalCommune = communeRepository.findById(dto.getCommuneId());
        if (!optionalCommune.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Không tìm thấy Xã với id: " + dto.getCommuneId())
                    .addPropertyNode("communeId")
                    .addConstraintViolation();
            return false;
        }
        Commune commune = optionalCommune.get();

        if (commune.getDistrict() == null || !commune.getDistrict().getId().equals(dto.getDistrictId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Xã không thuộc Huyện được chỉ định")
                    .addPropertyNode("communeId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
