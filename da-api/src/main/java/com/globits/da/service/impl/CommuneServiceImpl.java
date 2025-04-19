package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.exception.CommuneNotFoundException;
import com.globits.da.exception.DuplicateEntryException;
import com.globits.da.exception.ErrorCode;
import com.globits.da.mapper.CommuneMapper;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommuneServiceImpl extends GenericServiceImpl<Commune, Long> implements com.globits.da.service.CommuneService {

    private final CommuneRepository communeRepository;
    private final CommuneMapper communeMapper;
    private final DistrictRepository districtRepository;


    public CommuneServiceImpl(CommuneRepository communeRepository, CommuneMapper communeMapper, DistrictRepository districtRepository) {
        this.communeRepository = communeRepository;
        this.communeMapper = communeMapper;
        this.districtRepository = districtRepository;
    }

    @Override
    public CommuneResponseDto saveCommune(CommuneRequestDto dto) {
        boolean existsByCode = communeRepository.existsByCode(dto.getCode());
        if (existsByCode) {
            throw new DuplicateEntryException("Commune code already exists");
        }
        Commune commune = communeMapper.toEntity(dto);
        communeRepository.save(commune);

        return communeMapper.toDto(commune);
    }

    @Override
    public List<Commune> findAllCommunes() {
        return communeRepository.findAll();
    }

    @Override
    public CommuneResponseDto findCommune(Long id) {
        Commune commune = communeRepository.findById(id)
                .orElseThrow(() -> new CommuneNotFoundException(id));

        return communeMapper.toDto(commune);
    }


    @Override
    public CommuneResponseDto updateCommune(Long id, CommuneRequestDto dto) {
        return communeMapper.toDto(communeRepository.findById(id).map(commune -> {
            boolean existsByCode = communeRepository.existsByCode(dto.getCode());
            if (existsByCode) {
                throw new DuplicateEntryException("Commune code already exists");
            }
            commune.setName(dto.getName());
            commune.setCode(dto.getCode());
            District district = districtRepository.findById(dto.getDistrictId())
                    .orElseThrow(() -> new RuntimeException("District not found"));
            commune.setDistrict(district);

            return communeRepository.save(commune);
        }).orElseThrow(() -> new CommuneNotFoundException(id)));
    }

    @Override
    public void deleteCommune(Long id) {
        Commune commune = communeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.BAD_REQUEST.getMessage()));
        communeRepository.deleteById(commune.getId());
    }
}
