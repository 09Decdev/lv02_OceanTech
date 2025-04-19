package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.entity.District;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.request.ProvinceRequestDto;
import com.globits.da.dto.response.ProvinceResponseDto;
import com.globits.da.exception.DuplicateEntryException;
import com.globits.da.exception.ProvinceNotFoundException;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.mapper.ProvincesMapper;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<Province, Long> implements com.globits.da.service.ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvincesMapper provincesMapper;
    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvincesMapper provincesMapper, DistrictRepository districtRepository, DistrictMapper districtMapper) {
        this.provinceRepository = provinceRepository;
        this.provincesMapper = provincesMapper;
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
    }


    @Override
    public List<ProvinceResponseDto> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provincesMapper.toDtoList(provinces);
    }

    @Override
    public ProvinceResponseDto getProvinceById(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new ProvinceNotFoundException(id));
        return provincesMapper.toDto(province);
    }

    @Override
    public ProvinceResponseDto createProvince(Province province, List<District> districts) {
        boolean existsByCode = provinceRepository.existsByCode(province.getCode());
        if (existsByCode) {
            throw new DuplicateEntryException("Province code already exists");
        }
        if (districts != null) {
            for (District district : districts) {
                boolean existCodeDistrict = districtRepository.existsByCode(district.getCode());
                boolean existNameDistrict = districtRepository.existsByName(district.getName());
                if (existCodeDistrict && existNameDistrict) {
                    throw new DuplicateEntryException("District already exists");
                }
                district.setProvince(province);
            }
            province.setDistricts(districts);
        }
        return provincesMapper.toDto(provinceRepository.save(province));
    }

    @Override
    public ProvinceResponseDto updateProvince(Long id, Province provinceUpdate, List<District> updateDistricts) {
        Province existing = provinceRepository.findById(id)
                .orElseThrow(() -> new ProvinceNotFoundException(id));

        boolean existsByCode = provinceRepository.existsByCode(provinceUpdate.getCode());
        if (existsByCode && !existing.getCode().equals(provinceUpdate.getCode())) {
            throw new DuplicateEntryException("Province code already exists");
        }

        existing.setName(provinceUpdate.getName());
        existing.setCode(provinceUpdate.getCode());

        List<District> currentDistricts = new ArrayList<>(existing.getDistricts());

        Iterator<District> iterator = existing.getDistricts().iterator();
        while (iterator.hasNext()) {
            District old = iterator.next();
            boolean stillExists = updateDistricts.stream().anyMatch(d -> d.getId() != null && d.getId().equals(old.getId()));
            if (!stillExists) {
                iterator.remove();
            }
        }

        if (updateDistricts != null) {
            for (District district : updateDistricts) {
                if (district.getId() == null || district.getId() == 0) {
                    boolean existCodeDistrict = districtRepository.existsByCode(district.getCode());
                    boolean existNameDistrict = districtRepository.existsByName(district.getName());
                    if (existCodeDistrict && existNameDistrict) {
                        throw new DuplicateEntryException("District already exists");
                    }

                    district.setProvince(existing);
                    existing.getDistricts().add(district); // add trực tiếp vào collection
                } else {
                    District existingDistrict = currentDistricts.stream()
                            .filter(d -> d.getId().equals(district.getId()))
                            .findFirst()
                            .orElse(null);
                    if (existingDistrict != null) {
                        existingDistrict.setName(district.getName());
                        existingDistrict.setCode(district.getCode());
                    }
                }
            }
        }

        return provincesMapper.toDto(provinceRepository.save(existing));
    }


    @Override
    public void deleteProvince(Long id) {
        Province existing = provinceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Province not found"));
        provinceRepository.deleteById(existing.getId());

    }

    @Override
    public ProvinceResponseDto createdProvince(ProvinceRequestDto provinceRequestDto) {
        if (provinceRepository.existsByCode(provinceRequestDto.getCode())) {
            throw new DuplicateEntryException("Province code already exists");
        }
        if (provinceRepository.existsByName(provinceRequestDto.getName())) {
            throw new DuplicateEntryException("Province name already exists");
        }
        Province province = new Province();
        province.setCode(provinceRequestDto.getCode());
        province.setName(provinceRequestDto.getName());

        List<DistrictRequestDto> districtRequestDtoList = provinceRequestDto.getDistrictId();
        if (districtRequestDtoList != null || !districtRequestDtoList.isEmpty()) {
            List<District> districtList = districtRequestDtoList.stream().map(districtDto -> {
                if (districtRepository.existsByCode(districtDto.getCode())) {
                    throw new DuplicateEntryException("District already exists");
                }
                if (districtRepository.existsByName(districtDto.getName())) {
                    throw new DuplicateEntryException("District name already exists");
                }
                District district = districtMapper.toEntity(districtDto);
                district.setProvince(province);
                return district;
            }).collect(Collectors.toList());

            province.setDistricts(districtList);
        }
        Province savedProvince = provinceRepository.save(province);
        return provincesMapper.toDto(savedProvince);
    }

    @Override
    public ProvinceResponseDto updatedProvince(Long id, ProvinceRequestDto provinceRequestDto) {
        Province existingProvince = provinceRepository.findById(id)
                .orElseThrow(() -> new ProvinceNotFoundException(id));

        existingProvince.setName(provinceRequestDto.getName());
        existingProvince.setCode(provinceRequestDto.getCode());

        if (provinceRequestDto.getDistrictId() != null && !provinceRequestDto.getDistrictId().isEmpty()) {
            List<District> currentDistricts = existingProvince.getDistricts();

            Map<Long, District> existingDistrictMap = currentDistricts.stream()
                    .filter(d -> d.getId() != null)
                    .collect(Collectors.toMap(District::getId, d -> d));

            List<District> updatedDistricts = new ArrayList<>();

            for (DistrictRequestDto dto : provinceRequestDto.getDistrictId()) {
                if (dto.getId() == null) {
                    if (districtRepository.existsByName(dto.getName())) {
                        throw new DuplicateEntryException("District name already exists: " + dto.getName());
                    }
                    if (districtRepository.existsByCode(dto.getCode())) {
                        throw new DuplicateEntryException("District code already exists: " + dto.getCode());
                    }
                    District newDistrict = new District();
                    newDistrict.setName(dto.getName());
                    newDistrict.setCode(dto.getCode());
                    newDistrict.setProvince(existingProvince);
                    updatedDistricts.add(newDistrict);
                } else {
                    District existingDistrict = existingDistrictMap.get(dto.getId());
                    if (existingDistrict == null) {
                        throw new DuplicateEntryException("District ID " + dto.getId() + " does not exist in this province");
                    }

                    if (!existingDistrict.getName().equals(dto.getName())
                            && districtRepository.existsByName(dto.getName())) {
                        throw new DuplicateEntryException("District name already exists: " + dto.getName());
                    }

                    if (!existingDistrict.getCode().equals(dto.getCode())
                            && districtRepository.existsByCode(dto.getCode())) {
                        throw new DuplicateEntryException("District code already exists: " + dto.getCode());
                    }

                    existingDistrict.setName(dto.getName());
                    existingDistrict.setCode(dto.getCode());
                    updatedDistricts.add(existingDistrict);

                    existingDistrictMap.remove(dto.getId());
                }
            }

            currentDistricts.clear();
            currentDistricts.addAll(updatedDistricts);
        }

        Province savedProvince = provinceRepository.save(existingProvince);
        return provincesMapper.toDto(savedProvince);
    }

}

