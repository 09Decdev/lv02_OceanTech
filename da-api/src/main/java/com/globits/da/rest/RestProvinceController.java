package com.globits.da.rest;

import com.globits.da.mapper.ProvincesMapper;
import com.globits.da.domain.entity.Province;
import com.globits.da.dto.request.ProvinceRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.ProvinceResponseDto;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/province")
public class RestProvinceController {
    private final ProvinceService provinceService;

    private final ProvincesMapper provincesMapper;

    public RestProvinceController(ProvinceService provinceService, ProvincesMapper provincesMapper) {
        this.provinceService = provinceService;
        this.provincesMapper = provincesMapper;
    }

    @GetMapping
    public ApiResponse<List<ProvinceResponseDto>> getAllProvinces() {
        ApiResponse<List<ProvinceResponseDto>> result = new ApiResponse<>();
        result.setData(provinceService.getAllProvinces());
        return result;
    }

    @GetMapping("/{id}")
    public ApiResponse<ProvinceResponseDto> getProvince(@PathVariable Long id) {
        ApiResponse<ProvinceResponseDto> result = new ApiResponse<>();
        result.setData(provinceService.getProvinceById(id));
        return result;
    }

    @PostMapping("/create")
    public ApiResponse<ProvinceResponseDto> createProvince(@RequestBody ProvinceRequestDto provinceRequestDto) {
        ApiResponse<ProvinceResponseDto> result = new ApiResponse<>();
        result.setData(provinceService.createdProvince(provinceRequestDto));
        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<ProvinceResponseDto> updateProvince(@RequestBody ProvinceRequestDto dto
            , @PathVariable("id") Long id) {
        ApiResponse<ProvinceResponseDto> result = new ApiResponse<>();
        result.setData(provinceService.updatedProvince(id, dto));
        return result;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteProvince(@PathVariable Long id) {
        ApiResponse<Boolean> result = new ApiResponse<>();
        provinceService.deleteProvince(id);
        result.setData(Boolean.TRUE);
        result.setMessage("Deleted province with id " + id);
        return result;
    }
}
