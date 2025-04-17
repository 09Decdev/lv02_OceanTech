package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.exception.DistrictNotFoundException;
import com.globits.da.exception.DuplicateEntryException;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.DistrictService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, Long> implements DistrictService {
    private final DistrictRepository districtRepository;

    private final DistrictMapper districtMapper;

    private final CommuneRepository communeRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository, DistrictMapper districtMapper, CommuneRepository communeRepository) {
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
        this.communeRepository = communeRepository;
    }

    @Override
    public List<DistrictResponseDto> listAllDistricts() {
        List<District> districts = districtRepository.findAll();
        return districtMapper.toDtoList(districts);
    }

    @Override
    public DistrictResponseDto getDistrictById(Long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new DistrictNotFoundException(id));
        return districtMapper.toDto(district);
    }

    @Override
    public District addNewDistrict(District district, List<Commune> communeList) {
        if (communeList != null) {
            if (districtRepository.existsByCode(district.getCode())) {
                throw new DuplicateEntryException("District code already exists: " + district.getCode());
            }
            if (districtRepository.existsByName(district.getName())) {
                throw new DuplicateEntryException("District name already exists: " + district.getName());
            }
            for (Commune commune : communeList) {
                if (communeRepository.existsByCode(commune.getCode())) {
                    throw new DuplicateEntryException("Commune code already exists: " + commune.getCode());
                }
                if (communeRepository.existsByName(commune.getName())) {
                    throw new DuplicateEntryException("Commune name already exists: " + commune.getName());
                }
                commune.setDistrict(district);
            }
            district.setCommunes(communeList);
        }
        return districtRepository.save(district);
    }

    @Override
    public void removeDistrict(Long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new DistrictNotFoundException(id));
        districtRepository.deleteById(district.getId());
    }

    @Override
    public District updateDistrict(Long id, District updateDistrict, List<Commune> communeList) {
        District existingDistrict = districtRepository.findById(id)
                .orElseThrow(() -> new DistrictNotFoundException(id));

        updateDistrictDetails(existingDistrict, updateDistrict);

        if (communeList != null) {
            validateAndUpdateCommunes(communeList, existingDistrict);
        }

        return districtRepository.save(existingDistrict);
    }

    private void updateDistrictDetails(District existingDistrict, District updateDistrict) {
        existingDistrict.setName(updateDistrict.getName());
        existingDistrict.setCode(updateDistrict.getCode());
    }

    private void validateAndUpdateCommunes(List<Commune> communeList, District existingDistrict) {
        List<Commune> currentCommunes = existingDistrict.getCommunes();
        Map<Long, Commune> currentCommuneMap = currentCommunes.stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(Commune::getId, c -> c));

        List<Commune> finalCommunes = new ArrayList<>();

        for (Commune commune : communeList) {
            if (commune.getId() == null) {
                validateNewCommune(commune);
                commune.setDistrict(existingDistrict);
                finalCommunes.add(commune);
            } else {
                updateExistingCommune(commune, currentCommuneMap, finalCommunes);
            }
        }

        existingDistrict.setCommunes(finalCommunes);
    }

    private void validateNewCommune(Commune commune) {
        if (communeRepository.existsByCode(commune.getCode())) {
            throw new DuplicateEntryException("Commune code already exists: " + commune.getCode());
        }
        if (communeRepository.existsByName(commune.getName())) {
            throw new DuplicateEntryException("Commune name already exists: " + commune.getName());
        }
    }

    private void updateExistingCommune(Commune commune, Map<Long, Commune> currentCommuneMap, List<Commune> finalCommunes) {
        Commune existingCommune = currentCommuneMap.get(commune.getId());
        if (existingCommune != null) {
            existingCommune.setName(commune.getName());
            existingCommune.setCode(commune.getCode());
            finalCommunes.add(existingCommune);
            currentCommuneMap.remove(existingCommune.getId());
        } else {
            throw new DuplicateEntryException("Commune ID " + commune.getId() + " does not exist for update!");
        }
    }


}
