package com.globits.da.repository;

import com.globits.da.domain.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    District findOneByCode(String code);
    District findOneByName(String name);
    District findOneByCodeAndName(String code, String name);
    District findOneByCodeOrName(String code, String name);
    List<District> findByProvinceId(Long provinceId);

    boolean existsByCode(String code);
    boolean existsByName(String name);
}
