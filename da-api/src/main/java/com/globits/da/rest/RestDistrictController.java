package com.globits.da.rest;

import com.globits.da.domain.entity.District;
import com.globits.da.dto.request.DistrictRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.DistrictResponseDto;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.service.DistrictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/district")
public class RestDistrictController {

    private final DistrictService districtService;

    private final DistrictMapper districtMapper;

    public RestDistrictController(DistrictService districtService, DistrictMapper districtMapper) {
        this.districtService = districtService;
        this.districtMapper = districtMapper;
    }

    @GetMapping
    public ApiResponse<List<DistrictResponseDto>> getAllDistricts() {
        ApiResponse<List<DistrictResponseDto>> result = new ApiResponse<>();
        result.setData(districtService.listAllDistricts());

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
        result.setData(districtService.addNewDistrict(dto));
        result.setMessage("success");

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<DistrictResponseDto> updateDistrict(@PathVariable Long id, @RequestBody DistrictRequestDto dto) {
        ApiResponse<DistrictResponseDto> result = new ApiResponse<>();
        result.setData(districtService.updateDistrict(id,dto));
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
