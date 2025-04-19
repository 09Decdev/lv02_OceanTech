package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.exception.DistrictNotFoundException;
import com.globits.da.exception.DuplicateEntryException;
import com.globits.da.exception.ProvinceNotFoundException;
import com.globits.da.mapper.CommuneMapper;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
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
    private final CommuneMapper communeMapper;
    private final ProvinceRepository provinceRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository, DistrictMapper districtMapper, CommuneRepository communeRepository, CommuneMapper communeMapper, ProvinceRepository provinceRepository) {
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
        this.communeRepository = communeRepository;
        this.communeMapper = communeMapper;
        this.provinceRepository = provinceRepository;
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
    public DistrictResponseDto addNewDistrict(DistrictRequestDto dto) {

        if (districtRepository.existsByCode(dto.getCode())) {
            throw new DuplicateEntryException("District code already exists: " + dto.getCode());
        }
        if (districtRepository.existsByName(dto.getName())) {
            throw new DuplicateEntryException("District name already exists: " + dto.getName());
        }
        Province province = provinceRepository.findById(dto.getProvinceId())
                .orElseThrow(() -> new ProvinceNotFoundException(dto.getProvinceId()));

        District district = new District();
        district.setName(dto.getName());
        district.setCode(dto.getCode());
        district.setProvince(province);

        List<CommuneRequestDto> communeDtoList = dto.getCommuneId();
        if (communeDtoList != null && !communeDtoList.isEmpty()) {
            List<Commune> communeList = communeDtoList.stream()
                    .map(communeDto -> {
                        if (communeRepository.existsByCode(communeDto.getCode())) {
                            throw new DuplicateEntryException("Commune code already exists: " + communeDto.getCode());
                        }
                        if (communeRepository.existsByName(communeDto.getName())) {
                            throw new DuplicateEntryException("Commune name already exists: " + communeDto.getName());
                        }
                        Commune commune = communeMapper.toEntity(communeDto);
                        commune.setDistrict(district);
                        return commune;
                    }).collect(Collectors.toList());

            district.setCommunes(communeList);
        }

        District savedDistrict = districtRepository.save(district);
        return districtMapper.toDto(savedDistrict);
    }


    @Override
    public void removeDistrict(Long id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new DistrictNotFoundException(id));
        districtRepository.deleteById(district.getId());
    }

    @Override
    public DistrictResponseDto updateDistrict(Long id, DistrictRequestDto dto) {
        District existingDistrict = districtRepository.findById(id)
                .orElseThrow(() -> new DistrictNotFoundException(id));

        existingDistrict.setName(dto.getName());
        existingDistrict.setCode(dto.getCode());

        if (dto.getCommuneId() != null && !dto.getCommuneId().isEmpty()) {
            List<Commune> currentCommunes = existingDistrict.getCommunes();
            Map<Long, Commune> currentCommuneMap = currentCommunes.stream()
                    .filter(c -> c.getId() != null)
                    .collect(Collectors.toMap(Commune::getId, c -> c));

            List<Commune> finalCommunes = new ArrayList<>();

            for (CommuneRequestDto communeDto : dto.getCommuneId()) {
                if (communeDto.getId() == null) {
                    if (communeRepository.existsByCode(communeDto.getCode())) {
                        throw new DuplicateEntryException("Commune code already exists: " + communeDto.getCode());
                    }
                    if (communeRepository.existsByName(communeDto.getName())) {
                        throw new DuplicateEntryException("Commune name already exists: " + communeDto.getName());
                    }

                    Commune newCommune = new Commune();
                    newCommune.setName(communeDto.getName());
                    newCommune.setCode(communeDto.getCode());
                    newCommune.setDistrict(existingDistrict);
                    finalCommunes.add(newCommune);
                } else {
                    Commune existingCommune = currentCommuneMap.get(communeDto.getId());
                    if (existingCommune != null) {
                        existingCommune.setName(communeDto.getName());
                        existingCommune.setCode(communeDto.getCode());
                        finalCommunes.add(existingCommune);
                        currentCommuneMap.remove(existingCommune.getId());
                    } else {
                        throw new DuplicateEntryException("Commune ID " + communeDto.getId() + " does not exist for update!");
                    }
                }
            }

            existingDistrict.setCommunes(finalCommunes);
        }

        District updatedDistrict = districtRepository.save(existingDistrict);
        return districtMapper.toDto(updatedDistrict);
    }


}
