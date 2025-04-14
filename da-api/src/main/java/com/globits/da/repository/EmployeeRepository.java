package com.globits.da.repository;

import com.globits.da.domain.entity.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.response.EmployeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    @Query("select(e.id) from Employee e WHERE e.id = ?1")
    Long checkIdExists(Long id);

    @Query("select new com.globits.da.dto.response.EmployeeResponseDto(ed) from Employee ed")
    Page<EmployeeResponseDto> getListPage(Pageable pageable);

    @Query("select new com.globits.da.dto.EmployeeDto(ed) from Employee ed")
    List<EmployeeDto>getAllEmployee();

    boolean existsByCode(String code);


}
