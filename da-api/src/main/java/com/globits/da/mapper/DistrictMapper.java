package com.globits.da.mapper;

import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DistrictMapper {
    @Autowired
    private CommuneMapper communeMapper;
    @Autowired
    private ProvinceRepository provinceRepository;

    public District toEntity(DistrictRequestDto dto) {
        if (dto == null) {
            return null;
        }

        District district = new District();
        district.setId(dto.getId());
        district.setName(dto.getName());
        district.setCode(dto.getCode());

        if (dto.getProvinceId() != null) {
            provinceRepository.findById(dto.getProvinceId()).ifPresent(district::setProvince);
        }

        if (dto.getCommuneId() != null) {
            List<Commune> communeList = new ArrayList<>();
            for (CommuneRequestDto commune : dto.getCommuneId()) {
                Commune commune1 = communeMapper.toEntity(commune);
                commune1.setDistrict(district);
                communeList.add(commune1);
            }
            district.setCommunes(communeList);
        }
        return district;
    }

    public DistrictResponseDto toDto(District district) {
        if (district == null) {
            return null;
        }
        DistrictResponseDto dto = new DistrictResponseDto();
        dto.setId(district.getId());
        dto.setName(district.getName());
        dto.setCode(district.getCode());

        if (district.getCommunes() != null) {
            List<CommuneResponseDto> communeResponseDtos = new ArrayList<>();
            for (Commune commune : district.getCommunes()) {
                communeResponseDtos.add(toCommuneDto(commune));
            }
            dto.setCommunes(communeResponseDtos);
        }
        return dto;
    }

    public List<DistrictResponseDto> toDtoList(List<com.globits.da.domain.entity.District> districts) {
        if (districts == null) {
            return Collections.emptyList();
        }

        List<DistrictResponseDto> result = new ArrayList<>();
        for (District district : districts) {
            if (district != null) {
                DistrictResponseDto dto = toDto(district);
                if (dto != null) {
                    result.add(dto);
                }
            }

        }
        return result;
    }

    public CommuneResponseDto toCommuneDto(Commune commune) {
        if (commune == null) {
            return null;
        }
        CommuneResponseDto dto = new CommuneResponseDto();
        dto.setId(commune.getId());
        dto.setName(commune.getName());
        dto.setCode(commune.getCode());
        return dto;
    }


}

