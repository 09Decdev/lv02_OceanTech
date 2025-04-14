package com.globits.da.repository;

import com.globits.da.domain.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>  {
boolean existsByNameAndProvince_IdAndEmployee_IdAndExpiryDateAfter(
        String name,
        Long provinceId,
        Long employeeId,
        LocalDate currentDate
);
    long countByNameAndEmployee_IdAndExpiryDateAfter(
            String name,
            Long employeeId,
            LocalDate currentDate
    );
}
