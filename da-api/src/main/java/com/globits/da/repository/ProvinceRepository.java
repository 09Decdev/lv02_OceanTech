package com.globits.da.repository;

import com.globits.da.domain.entity.Province;
import org.jadira.usertype.spi.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Province findOneByCode(String code);
    Province findOneByName(String name);
    Province findOneByCodeAndName(String code, String name);
    Province findOneByCodeOrName(String code, String name);

    Long id(Long id);
}
