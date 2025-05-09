package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.entity.Employee;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.dto.response.ImportError;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface EmployeeService extends GenericService<Employee, Long> {
    Page<EmployeeResponseDto> getPage(int pageSize, int pageIndex);

    EmployeeResponseDto saveEmployee(EmployeeRequestDto dto);

    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto);

    Boolean deleteEmployee(Long id);

    EmployeeResponseDto addCertificate(Long id, List<Long> certificateIds);

    List<ImportError> importEmployeesFromExcel(MultipartFile file);

    EmployeeResponseDto getEmployee(Long id);

    List<EmployeeResponseDto> getEmployees();
}
