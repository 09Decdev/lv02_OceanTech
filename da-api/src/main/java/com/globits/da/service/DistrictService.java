package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.response.DistrictResponseDto;


import java.util.List;

public interface DistrictService extends GenericService<District, Long> {
    List<DistrictResponseDto> listAllDistricts();
    DistrictResponseDto getDistrictById(Long id);
    District addNewDistrict(District district, List<Commune>communeList);
    District updateDistrict(Long id, District updateDistrict, List<Commune>communeList);
    void removeDistrict(Long id);

}
