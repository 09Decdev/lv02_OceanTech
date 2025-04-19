package com.globits.da.rest;

import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.mapper.CommuneMapper;
import com.globits.da.service.CommuneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commune")
public class RestCommuneController {

    private final CommuneService communeService;

    private final CommuneMapper communeMapper;

    public RestCommuneController(CommuneService communeService, CommuneMapper communeMapper) {
        this.communeService = communeService;
        this.communeMapper = communeMapper;
    }

    @GetMapping
    public ApiResponse<List<CommuneResponseDto>> getAllCommune() {
        ApiResponse<List<CommuneResponseDto>> result = new ApiResponse<>();
        List<Commune> communeList = communeService.findAllCommunes();
        List<CommuneResponseDto> communeDtoList = communeMapper.toDtoList(communeList);
        result.setData(communeDtoList);
        return result;
    }

    @GetMapping("/{id}")
    public ApiResponse<CommuneResponseDto> getCommune(@PathVariable Long id) {
        ApiResponse<CommuneResponseDto> result = new ApiResponse<>();
        result.setData(communeService.findCommune(id));

        return result;
    }

    @PostMapping("create")
    public ApiResponse<CommuneResponseDto> saveCommune(@RequestBody CommuneRequestDto communeRequestDto) {
        ApiResponse<CommuneResponseDto> result = new ApiResponse<>();
        result.setData(communeService.saveCommune(communeRequestDto));

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<CommuneResponseDto> updateCommune(@RequestBody CommuneRequestDto dto, @PathVariable Long id) {
        ApiResponse<CommuneResponseDto> result = new ApiResponse<>();
        result.setData(communeService.updateCommune(id,dto));

        return result;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCommune(@PathVariable Long id) {
        communeService.deleteCommune(id);
        ApiResponse<String> result = new ApiResponse<>();
        result.setMessage("Commune deleted successfully");
        return result;
    }
}
