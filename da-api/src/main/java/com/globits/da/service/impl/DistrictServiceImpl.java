package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Exception.ErrorCode;
import com.globits.da.Mapper.DistrictMapper;
import com.globits.da.domain.entity.Commune;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl extends GenericServiceImpl<District,Long> implements DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private CommuneRepository communeRepository;


    @Override
    public List<DistrictResponseDto> listAllDistricts() {
        List<District> districts = districtRepository.findAll();
        return districtMapper.toDtoList(districts)  ;
    }

    @Override
    public DistrictResponseDto getDistrictById(Long id) {
        District district=districtRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Not found District with id "+id));
        return districtMapper.toDto(district);
    }

    @Override
    public District addNewDistrict(District district, List<Commune> communeList) {
       if (communeList!=null){
           if (districtRepository.existsByCode(district.getCode())) {
               throw new RuntimeException("Mã District đã tồn tại: " + district.getCode());
           }
           if (districtRepository.existsByName(district.getName())) {
               throw new RuntimeException("Tên District đã tồn tại: " + district.getName());
           }
           for (Commune commune:communeList){
               if (communeRepository.existsByCode(commune.getCode())){
                   throw new RuntimeException("Mã Commune đã tồn tại: " + commune.getCode());
               }
               if (communeRepository.existsByName(commune.getName())) {
                   throw new RuntimeException("Tên Commune đã tồn tại: " + commune.getName());
               }
               commune.setDistrict(district);
           }
           district.setCommunes(communeList);
       }
       return districtRepository.save(district);
    }

    @Override
    public District updateDistrict(Long id, District updateDistrict, List<Commune> communeList) {
      District existingDistrict=districtRepository.findById(id)
              .orElseThrow(()->new RuntimeException("Not found District with id "+id));

      existingDistrict.setName(updateDistrict.getName());
      existingDistrict.setCode(updateDistrict.getCode());

      List<Commune> currentCommune = existingDistrict.getCommunes();
        Map<Long,Commune> currentCommuneMap= currentCommune.stream()
                .filter(c -> c.getId()!=null)
                .collect(Collectors.toMap(Commune::getId, c -> c));

        List<Commune> finalCommunes = new ArrayList<>();
        if (communeList != null) {
            // Debug: In ra các id của commune từ request
            System.out.println("Commune list from request:");
            for (Commune commune : communeList) {
                System.out.println("Commune ID: " + commune.getId() + ", Name: " + commune.getName());
            }

            for (Commune commune : communeList) {

                if (commune.getId() == null) {
                    if (communeRepository.existsByCode(commune.getCode())) {
                        throw new RuntimeException("Mã Commune đã tồn tại: " + commune.getCode());
                    }
                    if (communeRepository.existsByName(commune.getName())) {
                        throw new RuntimeException("Tên Commune đã tồn tại: " + commune.getName());
                    }
                    commune.setDistrict(existingDistrict);
                    finalCommunes.add(commune);
                } else {
                    Commune existingCommune = currentCommuneMap.get(commune.getId());
                    if (existingCommune != null) {
                        existingCommune.setName(commune.getName());
                        existingCommune.setCode(commune.getCode());
                        finalCommunes.add(existingCommune);
                        currentCommuneMap.remove(existingCommune.getId());
                    } else {
                        throw new RuntimeException("Commune ID " + commune.getId() + " không tồn tại để cập nhật!");
                    }
                }
            }
        }
                existingDistrict.setCommunes(finalCommunes);
                return districtRepository.save(existingDistrict);
    }


    @Override
    public void removeDistrict(Long id) {
        District district=districtRepository.findById(id).orElseThrow(()->new RuntimeException(ErrorCode.Not_Found.getMessage()));
        districtRepository.deleteById(district.getId());
    }
}
