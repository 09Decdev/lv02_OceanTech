package com.globits.da.service.impl;

import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.dto.response.ProvinceResponseDto;
import org.springframework.stereotype.Service;

@Service
public class LocationEmployeeServiceImpl {
    private final ProvinceServiceImpl provinceService;
    private final DistrictServiceImpl districtService;
    private final CommuneServiceImpl communeService;

    public LocationEmployeeServiceImpl(ProvinceServiceImpl provinceService, DistrictServiceImpl districtService, CommuneServiceImpl communeService) {
        this.provinceService = provinceService;
        this.districtService = districtService;
        this.communeService = communeService;
    }


    public ProvinceResponseDto getProvince(Long id) {
        return provinceService.getProvinceById(id);
    }

    public DistrictResponseDto getDistrict(Long id) {
        return districtService.getDistrictById(id);
    }

    public Commune getCommune(Long id) {
        return communeService.findCommune(id);
    }
}
