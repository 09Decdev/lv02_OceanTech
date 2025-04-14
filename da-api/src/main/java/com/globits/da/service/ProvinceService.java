package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.entity.District;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.ProvinceRequestDto;
import com.globits.da.dto.response.ProvinceResponseDto;

import java.util.List;

public interface ProvinceService extends GenericService<Province, Long> {
    List<ProvinceResponseDto> getAllProvinces();
    ProvinceResponseDto getProvinceById(Long id);
    Province createProvince(Province province, List<District>districts);
    Province updateProvince(Long id, Province provinceUpdate,List<District>updateDistricts);
    void deleteProvince(Long id);
}
