package com.globits.da.Mapper;

import com.globits.da.domain.entity.*;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private CertificateMapper certificateMapper;

    public Employee toEntity(EmployeeRequestDto dto) {
        if (dto == null) return null;
        Employee employee = new Employee();
        employee.setCode(dto.getCode());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAge(dto.getAge());

        if (dto.getDistrictId() != null && dto.getDistrictId() != null && dto.getCommuneId() != null) {
            Province province = provinceRepository.findById(dto.getProvinceId())
                    .orElseThrow(() -> new RuntimeException("Province not found with id: " + dto.getProvinceId()));

            District district = districtRepository.findById(dto.getDistrictId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + dto.getDistrictId()));

            Commune commune = communeRepository.findById(dto.getCommuneId())
                    .orElseThrow(() -> new RuntimeException("Commune not found with id: " + dto.getCommuneId()));

            employee.setProvince(province);
            employee.setDistrict(district);
            employee.setCommune(commune);
        }

        if (dto.getCertificateIds() != null && !dto.getCertificateIds().isEmpty()) {
            List<Certificate> certificates = certificateRepository.findAllById(dto.getCertificateIds());
            employee.setCertificate(certificates);
        }


        return employee;
    }

    public void updateEntity(Employee employee, EmployeeRequestDto dto) {
        if (dto == null || employee == null) return;

        employee.setCode(dto.getCode());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setAge(dto.getAge());

        if (dto.getDistrictId() != null && dto.getDistrictId() != null && dto.getCommuneId() != null) {
            Province province = provinceRepository.findById(dto.getProvinceId())
                    .orElseThrow(() -> new RuntimeException("Province not found with id: " + dto.getProvinceId()));

            District district = districtRepository.findById(dto.getDistrictId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + dto.getDistrictId()));

            Commune commune = communeRepository.findById(dto.getCommuneId())
                    .orElseThrow(() -> new RuntimeException("Commune not found with id: " + dto.getCommuneId()));

            employee.setProvince(province);
            employee.setDistrict(district);
            employee.setCommune(commune);
        }

    }

    public EmployeeResponseDto toDto(Employee employee) {
        if (employee == null) return null;
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setId(employee.getId());
        employeeResponseDto.setCode(employee.getCode());
        employeeResponseDto.setName(employee.getName());
        employeeResponseDto.setEmail(employee.getEmail());
        employeeResponseDto.setPhone(employee.getPhone());
        employeeResponseDto.setAge(employee.getAge());
        if (employee.getProvince() != null) {
            employeeResponseDto.setProvinceName(employee.getProvince().getName());
        } else {
            employeeResponseDto.setProvinceName(null);
        }
        if (employee.getDistrict() != null) {
            employeeResponseDto.setDistrictName(employee.getDistrict().getName());
        } else {
            employeeResponseDto.setDistrictName(null);
        }
        if (employee.getCommune() != null) {
            employeeResponseDto.setCommuneName(employee.getCommune().getName());
        } else {
            employeeResponseDto.setCommuneName(null);
        }
        if (employee.getCertificate() != null && !employee.getCertificate().isEmpty()) {
            employeeResponseDto.setCertificates(
                    employee.getCertificate().stream().map(certificateMapper::toDto).collect(Collectors.toList())
            );
        } else {
            employeeResponseDto.setCertificates(Collections.emptyList());
        }

        return employeeResponseDto;
    }
}
