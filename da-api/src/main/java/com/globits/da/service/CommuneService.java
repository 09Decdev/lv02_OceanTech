package com.globits.da.service;

import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.response.CommuneResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommuneService {
    List<Commune> findAllCommunes();
    Commune findCommune(Long id);
    Commune saveCommune(Commune commune);
    Commune updateCommune(Long id,Commune commune);
    void deleteCommune(Long id);
}
