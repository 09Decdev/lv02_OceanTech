package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Mapper.CertificateMapper;
import com.globits.da.Mapper.EmployeeMapper;
import com.globits.da.domain.entity.*;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.dto.response.ImportError;
import com.globits.da.repository.*;
import com.globits.da.service.EmployeeService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;

    @Override
    public Page<EmployeeResponseDto> getPage(int pageSize, int pageIndex) {
        Pageable pageable= PageRequest.of(pageIndex-1,pageSize);
        return employeeRepository.getListPage(pageable);
    }

    @Override
    @Transactional
    public Employee saveEmployee(EmployeeRequestDto dto) {
            boolean existing = employeeRepository.existsByCode(dto.getCode());
            if (existing) {
            throw new RuntimeException("Mã Nhân Viên Đã Tồn Tại");
            }
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

@Transactional
public EmployeeResponseDto addCertificate(Long id, List<Long> certificateIds) {
    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

    List<Certificate> certificatesToAdd = certificateRepository.findAllById(certificateIds);
    List<Certificate> existingCertificates = employee.getCertificate();

    for (Certificate newCertificate : certificatesToAdd) {
        List<Certificate> sameNameCertificates = existingCertificates.stream()
                .filter(c -> c.getName().equals(newCertificate.getName()))
                .collect(Collectors.toList());

        long validCount = sameNameCertificates.stream()
                .filter(c -> c.getExpiryDate().isAfter(LocalDate.now()))
                .count();

        if (validCount >= 3) {
            Optional<Certificate> expiredOne = sameNameCertificates.stream()
                    .filter(c -> c.getExpiryDate().isBefore(LocalDate.now()))
                    .findFirst();

            if (expiredOne.isPresent()) {

                Certificate expired = expiredOne.get();
                existingCertificates.remove(expired);
                newCertificate.setEmployee(employee);
                existingCertificates.add(newCertificate);
            } else {
                throw new RuntimeException("Nhân viên đã có 3 văn bằng " + newCertificate.getName() + " còn hiệu lực.");
            }
        } else {
            newCertificate.setEmployee(employee);
            existingCertificates.add(newCertificate);
        }
    }

    employee.setCertificate(existingCertificates);
    employee = employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
}

    @Transactional
    public List<ImportError> importEmployeesFromExcel(MultipartFile file) {
        List<ImportError> errors = new ArrayList<>();
        List<Employee> employeesToSave = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 0;

            for (Row row : sheet) {
                if (rowNum == 0) {
                    rowNum++;
                    continue;
                }

                try {
                    String code = getCellValue(row.getCell(0));
                    String name = getCellValue(row.getCell(1));
                    String email = getCellValue(row.getCell(2));
                    String phone = getCellValue(row.getCell(3));
                    Integer age = Integer.valueOf(getCellValue(row.getCell(4)));
                    Long provinceId = Long.valueOf(getCellValue(row.getCell(5)));
                    Long districtId = Long.valueOf(getCellValue(row.getCell(6)));
                    Long communeId = Long.valueOf(getCellValue(row.getCell(7)));

                    // validate: mã nhân viên
                    if (employeeRepository.existsByCode(code)) {
                        errors.add(new ImportError(rowNum + 1, "Mã nhân viên đã tồn tại: " + code));
                        continue;
                    }

                    Province province = provinceRepository.findById(provinceId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh ID: " + provinceId));
                    District district = districtRepository.findById(districtId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy huyện ID: " + districtId));
                    Commune commune = communeRepository.findById(communeId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy xã ID: " + communeId));

                    Employee emp = new Employee();
                    emp.setCode(code);
                    emp.setName(name);
                    emp.setEmail(email);
                    emp.setPhone(phone);
                    emp.setAge(age);
                    emp.setProvince(province);
                    emp.setDistrict(district);
                    emp.setCommune(commune);

                    employeesToSave.add(emp);
                } catch (Exception e) {
                    errors.add(new ImportError(rowNum + 1, "Lỗi dòng: " + e.getMessage()));
                }

                rowNum++;
            }

            if (!errors.isEmpty()) {
                return errors;
            }

            employeeRepository.saveAll(employeesToSave);

        } catch (IOException e) {
            errors.add(new ImportError(0, "Lỗi khi đọc file: " + e.getMessage()));
        }

        return errors;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return "";
    }

}
