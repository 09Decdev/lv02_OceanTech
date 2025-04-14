package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Exception.ErrorCode;
import com.globits.da.Mapper.ProvincesMapper;
import com.globits.da.domain.entity.District;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.request.ProvinceRequestDto;
import com.globits.da.dto.response.ProvinceResponseDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<Province,Long> implements com.globits.da.service.ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private ProvincesMapper provincesMapper;


    @Override
    public List<ProvinceResponseDto> getAllProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provincesMapper.toDtoList(provinces)  ;
    }

    @Override
    public ProvinceResponseDto getProvinceById(Long id) {
        Province province=provinceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Not found Province with id "+id));
        return provincesMapper.toDto(province);
    }

    @Override
    public Province createProvince(Province province, List<District> districts) {
        if (districts!=null) {
            for (District district:districts) {
                district.setProvince(province);
            }
            province.setDistricts(districts);
        }
        return provinceRepository.save(province);
    }

    @Override
    public Province updateProvince(Long id, Province provinceUpdate, List<District> updateDistricts) {
        Province existing = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found Province with id " + id));

        existing.setName(provinceUpdate.getName());
        existing.setCode(provinceUpdate.getCode());

        List<District> districts = existing.getDistricts();
        Map<Long, District> currentDistricts = districts.stream()
                .filter(district -> district.getId() != null)
                .collect(Collectors.toMap(District::getId, district -> district));

        List<District> finalDistricts = new ArrayList<>();
        if (updateDistricts != null) {
            for (District district : updateDistricts) {
                if (district.getId() == null || district.getId() == 0) {
                    district.setProvince(existing);
                    finalDistricts.add(district);
                } else {
                    District existingDistrict = currentDistricts.get(district.getId());
                    if (existingDistrict != null) {
                        existingDistrict.setName(district.getName());
                        existingDistrict.setCode(district.getCode());

                        finalDistricts.add(existingDistrict);
                        currentDistricts.remove(district.getId());
                    }
                }

            }
        }
        existing.setDistricts(finalDistricts);
        return provinceRepository.save(existing);
    }

    @Override
    public void deleteProvince(Long id) {
        Province existing=provinceRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Province not found"));
        provinceRepository.deleteById(existing.getId());

    }

}
