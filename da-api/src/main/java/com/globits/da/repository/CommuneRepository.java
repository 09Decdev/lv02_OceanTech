package com.globits.da.repository;

import com.globits.da.domain.entity.Commune;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommuneRepository extends JpaRepository<Commune, Long> {
//    @EntityGraph(attributePaths = {"district"})
//@Query("SELECT c FROM Commune c JOIN FETCH c.district WHERE c.id = :id")
//    Optional<Commune>findById(Long id);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
