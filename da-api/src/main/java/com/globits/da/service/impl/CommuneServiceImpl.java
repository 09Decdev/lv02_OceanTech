package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.exception.CommuneNotFoundException;
import com.globits.da.exception.DuplicateEntryException;
import com.globits.da.exception.ErrorCode;
import com.globits.da.domain.entity.Commune;
import com.globits.da.repository.CommuneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommuneServiceImpl extends GenericServiceImpl<Commune, Long> implements com.globits.da.service.CommuneService {

    private final CommuneRepository communeRepository;


    public CommuneServiceImpl(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    @Override
    public Commune saveCommune(Commune commune) {
        boolean existsByCode = communeRepository.existsByCode(commune.getCode());
        if (existsByCode) {
            throw new DuplicateEntryException("Commune code already exists");
        }
        return communeRepository.save(commune);
    }

    @Override
    public List<Commune> findAllCommunes() {
        return communeRepository.findAll();
    }

    @Override
    public Commune findCommune(Long id) {
        return communeRepository.findById(id)
                .orElseThrow(() -> new CommuneNotFoundException(id));
    }


    @Override
    public Commune updateCommune(Long id, Commune communedetail) {
        return communeRepository.findById(id).map(commune -> {
            boolean existsByCode = communeRepository.existsByCode(communedetail.getCode());
            if (existsByCode) {
                throw new DuplicateEntryException("Commune code already exists");
            }
            commune.setName(communedetail.getName());
            commune.setCode(communedetail.getCode());
            commune.setDistrict(communedetail.getDistrict());
            return communeRepository.save(commune);
        }).orElseThrow(() -> new CommuneNotFoundException(id));
    }

    @Override
    public void deleteCommune(Long id) {
        Commune commune = communeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.BAD_REQUEST.getMessage()));
        communeRepository.deleteById(commune.getId());
    }
}
