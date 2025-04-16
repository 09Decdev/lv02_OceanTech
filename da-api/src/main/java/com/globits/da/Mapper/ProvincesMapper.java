package com.globits.da.Mapper;


import com.globits.da.domain.entity.District;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.request.ProvinceRequestDto;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.dto.response.ProvinceResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ProvincesMapper {
    @Autowired
    private DistrictMapper districtMapper;

    public ProvinceResponseDto toDto(Province province) {
        if (province == null) {
            return null;
        }

        ProvinceResponseDto dto = new ProvinceResponseDto();
        dto.setId(province.getId());
        dto.setName(province.getName());
        dto.setCode(province.getCode());

        // Nếu Province có danh sách District, chuyển đổi từng District sang DistrictResponseDto
        if (province.getDistricts() != null) {
            dto.setDistricts(districtMapper.toDtoList(province.getDistricts()));
        }
        return dto;
    }


    public List<ProvinceResponseDto> toDtoList(List<Province> provinces) {
        if (provinces == null) {
            return Collections.emptyList();
        }

        List<ProvinceResponseDto> result = new ArrayList<>();

        for (Province province : provinces) {
            if (province != null) {
                result.add(toDto(province));
            }
        }
        return result;
    }

    // Chuyển đổi một District thành DistrictResponseDto
    public DistrictResponseDto toDistrictDto(District entity) {
        if (entity == null) {
            return null;
        }

        DistrictResponseDto dto = new DistrictResponseDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return dto;
    }

    public Province toEntity(ProvinceRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Province province = new Province();
        province.setName(dto.getName());
        province.setCode(dto.getCode());

        if (dto.getDistrictId() != null) {
            List<District> districts = new ArrayList<>();
            for (DistrictRequestDto requestDistrictDto : dto.getDistrictId()) {
                District district = districtMapper.toEntity(requestDistrictDto);
                district.setProvince(province);
                districts.add(district);
            }
            province.setDistricts(districts);
        }
        return province;
    }


}
