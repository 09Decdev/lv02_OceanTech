package com.globits.da.rest;

import com.globits.da.mapper.DistrictMapper;
import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/district")
public class RestDistrictController {
    @Autowired
    private DistrictService districtService;
    @Autowired
    private DistrictMapper districtMapper;

    @GetMapping
    public ApiResponse<List<DistrictResponseDto>> getAllDistricts() {
        ApiResponse<List<DistrictResponseDto>> result = new ApiResponse<>();
        result.setData(districtService.listAllDistricts());
        result.setMessage("success");

        return result;
    }

    @GetMapping("/{id}")
    public ApiResponse<DistrictResponseDto> getDistrict(@PathVariable Long id) {
        ApiResponse<DistrictResponseDto> result = new ApiResponse<>();
        result.setData(districtService.getDistrictById(id));
        result.setMessage("success");

        return result;
    }

    @PostMapping("/creat")
    public ApiResponse<DistrictResponseDto> saveDistrict(@RequestBody DistrictRequestDto dto) {
        ApiResponse<DistrictResponseDto> result = new ApiResponse<>();
        District district = districtMapper.toEntity(dto);
        District created = districtService.addNewDistrict(district, district.getCommunes());
        DistrictResponseDto responseDto = districtMapper.toDto(created);
        result.setData(responseDto);
        result.setMessage("success");

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<DistrictResponseDto> updateDistrict(@PathVariable Long id, @RequestBody DistrictRequestDto dto) {
        ApiResponse<DistrictResponseDto> result = new ApiResponse<>();
        District district = districtMapper.toEntity(dto);
        District updateDistrict = districtService.updateDistrict(id, district, district.getCommunes());
        DistrictResponseDto responseDto = districtMapper.toDto(updateDistrict);
        result.setData(responseDto);
        result.setMessage("successfully updated");

        return result;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDistrict(@PathVariable Long id) {
        ApiResponse<Void> result = new ApiResponse<>();
        districtService.removeDistrict(id);
        result.setMessage("District deleted successfully");
        return result;
    }
}
