package com.globits.da.service;

import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.request.CommuneRequestDto;
import com.globits.da.dto.response.CommuneResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommuneService {
    List<Commune> findAllCommunes();

    CommuneResponseDto findCommune(Long id);

    CommuneResponseDto saveCommune(CommuneRequestDto dto);

    CommuneResponseDto updateCommune(Long id, CommuneRequestDto dto);

    void deleteCommune(Long id);
}
