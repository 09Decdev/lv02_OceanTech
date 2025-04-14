package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Exception.ErrorCode;
import com.globits.da.Mapper.CommuneMapper;
import com.globits.da.domain.entity.Commune;
import com.globits.da.dto.response.CommuneResponseDto;
import com.globits.da.repository.CommuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommuneServiceImpl extends GenericServiceImpl<Commune,Long> implements com.globits.da.service.CommuneService {
    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private CommuneMapper communeMapper;

    @Override
    public Commune saveCommune(Commune commune) {
        return communeRepository.save(commune);
    }

    @Override
    public List<Commune> findAllCommunes() {
        return communeRepository.findAll();
    }

    @Override
    public Commune findCommune(Long id) {
        Commune commune=communeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Commune not found with id "+id));
        return commune;
    }


    @Override
    public Commune updateCommune(Long id, Commune communedetail) {
        return communeRepository.findById(id).map(commune->{
            commune.setName(communedetail.getName());
            commune.setCode(communedetail.getCode());
            commune.setDistrict(communedetail.getDistrict());
            return communeRepository.save(commune);
        }).orElseThrow(()->new RuntimeException("Commune not found with id "+id));
    }

    @Override
    public void  deleteCommune(Long id) {
        Commune commune=communeRepository.findById(id)
                .orElseThrow(()->new RuntimeException(ErrorCode.Not_Found.getMessage()));
        communeRepository.deleteById(commune.getId());
    }
}
