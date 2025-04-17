package com.globits.da.mapper;

import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.repository.DistrictRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommuneMapper {


    private final DistrictRepository districtRepository;

    public CommuneMapper(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public CommuneResponseDto toDto(Commune commune) {
        if (commune == null) {
            return null;
        }
        CommuneResponseDto dto = new CommuneResponseDto();
        dto.setId(commune.getId());
        dto.setName(commune.getName());
        dto.setCode(commune.getCode());
        dto.setDistrictId(commune.getDistrict() != null ? commune.getDistrict().getId() : null);

        return dto;
    }

    public List<CommuneResponseDto> toDtoList(List<Commune> communes) {
        if (communes == null) {
            return null;
        }

        List<CommuneResponseDto> result = new ArrayList<>();
        for (Commune commune : communes) {
            if (commune != null) {
                CommuneResponseDto dto = toDto(commune);
                if (dto != null) {
                    result.add(dto);
                }
            }
        }
        return result;
    }

    public Commune toEntity(CommuneRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Commune commune = new Commune();
        commune.setName(dto.getName());
        commune.setCode(dto.getCode());
        if (dto.getDistrictId() != null) {
            districtRepository.findById(dto.getDistrictId()).ifPresent(commune::setDistrict);
        }
        return commune;
    }
}
