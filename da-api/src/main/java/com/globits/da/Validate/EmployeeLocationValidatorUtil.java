package com.globits.da.Validate;

import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;

import java.util.Optional;

public class EmployeeLocationValidatorUtil {

    private final DistrictRepository districtRepository;
    private final CommuneRepository communeRepository;
    private final ProvinceRepository provinceRepository;

    public EmployeeLocationValidatorUtil(DistrictRepository districtRepository,
                                         CommuneRepository communeRepository,
                                         ProvinceRepository provinceRepository) {
        this.districtRepository = districtRepository;
        this.communeRepository = communeRepository;
        this.provinceRepository = provinceRepository;
    }

    public void validate(EmployeeRequestDto dto) {
        if (dto == null) {
            return;
        }

        boolean hasProvince = dto.getProvinceId() != null;
        boolean hasDistrict = dto.getDistrictId() != null;
        boolean hasCommune = dto.getCommuneId() != null;

        boolean allProvided = hasProvince && hasDistrict && hasCommune;
        boolean allEmpty = !hasProvince && !hasDistrict && !hasCommune;

        if (!allProvided && !allEmpty) {
            throw new RuntimeException("Must provide all Province, District, and Commune or leave all three empty");
        }

        if (allEmpty) {
            return;
        }
        Optional<Province> optionalProvince = provinceRepository.findById(dto.getProvinceId());
        if (!optionalProvince.isPresent()) {
            throw new RuntimeException("Province not found with id: " + dto.getProvinceId());
        }
        Optional<District> optionalDistrict = districtRepository.findById(dto.getDistrictId());
        if (!optionalDistrict.isPresent()) {
            throw new RuntimeException("District not found with id: " + dto.getDistrictId());
        }
        District district = optionalDistrict.get();

        if (district.getProvince() == null || !district.getProvince().getId().equals(dto.getProvinceId())) {
            throw new RuntimeException("District does not belong to the specified Province");
        }

        Optional<Commune> optionalCommune = communeRepository.findById(dto.getCommuneId());
        if (!optionalCommune.isPresent()) {
            throw new RuntimeException("Commune not found with id: " + dto.getCommuneId());
        }
        Commune commune = optionalCommune.get();

        if (commune.getDistrict() == null || !commune.getDistrict().getId().equals(dto.getDistrictId())) {
            throw new RuntimeException("Commune does not belong to the specified District");
        }
    }
}
