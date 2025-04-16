package com.globits.da.rest;

import com.globits.da.Mapper.CommuneMapper;
import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.service.CommuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commune")
public class RestCommuneController {
    @Autowired
    private CommuneService communeService;

    @Autowired
    private CommuneMapper communeMapper;

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
        Commune commune = communeService.findCommune(id);
        CommuneResponseDto communeDto = communeMapper.toDto(commune);
        result.setData(communeDto);

        return result;
    }

    @PostMapping("create")
    public ApiResponse<CommuneResponseDto> saveCommune(@RequestBody CommuneRequestDto communeRequestDto) {
        ApiResponse<CommuneResponseDto> result = new ApiResponse<>();
        Commune commune = communeMapper.toEntity(communeRequestDto);
        Commune communeSaved = communeService.saveCommune(commune);
        CommuneResponseDto communeResponseDto = communeMapper.toDto(communeSaved);
        result.setData(communeResponseDto);

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<CommuneResponseDto> updateCommune(@RequestBody CommuneRequestDto dto, @PathVariable Long id) {
        ApiResponse<CommuneResponseDto> result = new ApiResponse<>();
        Commune commune = communeMapper.toEntity(dto);
        Commune updatCommune = communeService.updateCommune(id, commune);
        CommuneResponseDto communeResponseDto = communeMapper.toDto(updatCommune);
        result.setData(communeResponseDto);

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
