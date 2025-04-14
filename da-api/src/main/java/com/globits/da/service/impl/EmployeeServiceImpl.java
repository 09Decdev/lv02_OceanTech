package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Mapper.CertificateMapper;
import com.globits.da.Mapper.EmployeeMapper;
import com.globits.da.domain.entity.Certificate;
import com.globits.da.domain.entity.Employee;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Long>implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CertificateMapper certificateMapper;

    @Override
    public Page<EmployeeResponseDto> getPage(int pageSize, int pageIndex) {
        Pageable pageable= PageRequest.of(pageIndex-1,pageSize);
        return employeeRepository.getListPage(pageable);
    }

    @Override
    public Employee saveEmployee(EmployeeRequestDto dto) {
        Employee employee = employeeMapper.toEntity(dto);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeRequestDto dto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Employee not found with id: "+id));
        employeeMapper.updateEntity(existingEmployee, dto);
        return employeeRepository.save(existingEmployee);
    }


    @Override
    public Boolean deleteEmployee(Long id) {
        if (id!=null){
            employeeRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Employee not found with id: "+id);
    }

    public EmployeeResponseDto addCertificate(Long id, List<Long> certificateIds) {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found with id: "+id));

        List<Certificate> certificates=certificateRepository.findAllById(certificateIds);

        for (Certificate certificate:certificates){
            boolean exist=employee.getCertificate().stream()
                    .anyMatch(existingCertificate->existingCertificate.getId().equals(certificate.getId())
                            && existingCertificate.getExpiryDate().isAfter(LocalDate.now()));
            if (exist){
                throw new RuntimeException("Certificate already exists with id: "+certificate.getId()+" and expiry date: "+certificate.getExpiryDate());
            }

            Long countSameName=employee.getCertificate().stream().filter(existingCertificate->existingCertificate.getName().equals(certificate.getName())
                    && existingCertificate.getExpiryDate().isAfter(LocalDate.now())).count();

            if (countSameName>=3){
            throw new RuntimeException("Nhân viên đã có 3 văn bằng " + certificate.getName() + " còn hiệu lực.");
            }
            employee.getCertificate().add(certificate);
        }
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

}
