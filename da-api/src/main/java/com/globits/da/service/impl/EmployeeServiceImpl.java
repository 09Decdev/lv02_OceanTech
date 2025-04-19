package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Validate.EmployeeLocationValidatorUtil;
import com.globits.da.domain.entity.*;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.dto.response.ImportError;
import com.globits.da.exception.*;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.repository.*;
import com.globits.da.service.EmployeeService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Long> implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    private final EmployeeMapper employeeMapper;


    private final CertificateRepository certificateRepository;


    private final ProvinceRepository provinceRepository;


    private final DistrictRepository districtRepository;

    private final CommuneRepository communeRepository;

    private final EmployeeLocationValidatorUtil locationValidator;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, CertificateRepository certificateRepository,
                               DistrictRepository districtRepository,
                               CommuneRepository communeRepository,
                               ProvinceRepository provinceRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.certificateRepository = certificateRepository;
        this.districtRepository = districtRepository;
        this.communeRepository = communeRepository;
        this.provinceRepository = provinceRepository;
        this.locationValidator = new EmployeeLocationValidatorUtil(districtRepository, communeRepository, provinceRepository);
    }

    @Override
    public Page<EmployeeResponseDto> getPage(int pageSize, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return employeeRepository.getListPage(pageable);
    }

    @Override
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto dto) {
        if (employeeRepository.existsByCode(dto.getCode())) {
            throw new EmployeeCodeExistsException(dto.getCode());
        }

        locationValidator.validate(dto);

        Employee employee = employeeMapper.toEntity(dto);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.Not_Found_EMPLOYEE.getMessage() + id));

        if (employeeRepository.existsByCode(dto.getCode())) {
            throw new EmployeeCodeExistsException(dto.getCode());
        }

        locationValidator.validate(dto);

        employeeMapper.updateEntity(existingEmployee, dto);
        return employeeMapper.toDto(employeeRepository.save(existingEmployee));
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        Employee checkExisting = employeeRepository.findById(id)
                .orElseThrow(() -> new DuplicateEntryException(ErrorCode.Not_Found_EMPLOYEE.getMessage() + id));
        employeeRepository.delete(checkExisting);
        return true;
    }

    @Override
    public EmployeeResponseDto getEmployee(Long id) {
        return employeeMapper.toDto(employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id)));
    }

    @Override
    public List<EmployeeResponseDto> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());

    }

    /**
     Nghiệp vụ:
     - Một Employee sẽ có nhiều văn bằng
     - Mỗi văn bằng sẽ do một tỉnh cấp
     Yêu cầu:
     - Thêm văn bằng cho nhân viên - văn bằng sẽ do 1 tỉnh cụ thể cung cấp (1 List)
     - Nếu Employee đã có  văn bằng A của tỉnh X cung cấp thì không được thêm mới văn bằng A được tỉnh X nếu văn bằng đó còn hiệu lực.
     - Employee không được có quá 3 văn bằng cùng loại còn hiệu lực (Bất kể tỉnh nào cung cấp)
     **/
    @Transactional
    public EmployeeResponseDto addCertificate(Long id, List<Long> certificateIds) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.Not_Found_EMPLOYEE.getMessage() + id));

        List<Certificate> certificatesToAdd = certificateRepository.findAllById(certificateIds);
        List<Certificate> existingCertificates = employee.getCertificate();

        for (Certificate newCertificate : certificatesToAdd) {
            Optional<Certificate> duplicateValid = findDuplicateValidCertificate(existingCertificates, newCertificate);
            if (duplicateValid.isPresent()) {
                throw new DuplicateEntryException("Văn bằng " + newCertificate.getName() + " đã tồn tại và còn thời hạn!");
            }

            List<Certificate> sameTypeCertificates = existingCertificates.stream()
                    .filter(c -> c.getCertificateType().equals(newCertificate.getCertificateType()))
                    .collect(Collectors.toList());

            long validCount = sameTypeCertificates.stream()
                    .filter(c -> c.getExpiryDate().isAfter(LocalDate.now()))
                    .count();

            if (validCount >= 3) {
                Optional<Certificate> expiredOne = sameTypeCertificates.stream()
                        .filter(c -> c.getExpiryDate().isBefore(LocalDate.now()))
                        .findFirst();

                if (expiredOne.isPresent()) {
                    Certificate expired = expiredOne.get();
                    existingCertificates.remove(expired);
                    newCertificate.setEmployee(employee);
                    existingCertificates.add(newCertificate);
                } else {
                    throw new CertificateNotValidException(newCertificate.getName());
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

    private Optional<Certificate> findDuplicateValidCertificate(List<Certificate> existingCertificates, Certificate newCertificate) {
        return existingCertificates.stream()
                .filter(existing ->
                        existing.getName().equalsIgnoreCase(newCertificate.getName()) &&
                                existing.getCertificateType() == newCertificate.getCertificateType() &&
                                existing.getProvince().getId().equals(newCertificate.getProvince().getId()) &&
                                existing.getExpiryDate().isAfter(LocalDate.now()) // kiểm tra còn hạn
                )
                .findFirst();
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
                    ImportError rowError = processRow(row, rowNum);
                    if (rowError != null) {
                        errors.add(rowError);
                    } else {
                        Employee emp = createEmployeeFromRow(row);
                        employeesToSave.add(emp);
                    }
                } catch (Exception e) {
                    errors.add(new ImportError(rowNum + 1, "Row error: " + e.getMessage()));
                }
                rowNum++;
            }

            if (!errors.isEmpty()) {
                return errors;
            }

            employeeRepository.saveAll(employeesToSave);  // Lưu tất cả nhân viên
        } catch (IOException e) {
            errors.add(new ImportError(0, "Error reading file: " + e.getMessage()));
        }

        return errors;
    }

    private ImportError processRow(Row row, int rowNum) {
        try {
            String code = getCellValue(row.getCell(0));
            String name = getCellValue(row.getCell(1));
            String email = getCellValue(row.getCell(2));
            String phone = getCellValue(row.getCell(3));
            Integer age = Integer.valueOf(getCellValue(row.getCell(4)));
            Long provinceId = Long.valueOf(getCellValue(row.getCell(5)));
            Long districtId = Long.valueOf(getCellValue(row.getCell(6)));
            Long communeId = Long.valueOf(getCellValue(row.getCell(7)));

            if (employeeRepository.existsByCode(code)) {
                return new ImportError(rowNum + 1, "Employee code already exists: " + code);
            }

            EmployeeRequestDto dto = new EmployeeRequestDto();
            dto.setCode(code);
            dto.setName(name);
            dto.setEmail(email);
            dto.setPhone(phone);
            dto.setAge(age);
            dto.setProvinceId(provinceId);
            dto.setDistrictId(districtId);
            dto.setCommuneId(communeId);

            locationValidator.validate(dto);

            validateLocation(provinceId, districtId, communeId);

            return null;
        } catch (Exception e) {
            return new ImportError(rowNum + 1, "Row processing error: " + e.getMessage());
        }
    }

    private void validateLocation(Long provinceId, Long districtId, Long communeId) {
        provinceRepository.findById(provinceId)
                .orElseThrow(() -> new ProvinceNotFoundException(provinceId));
        districtRepository.findById(districtId)
                .orElseThrow(() -> new DistrictNotFoundException(districtId));
        communeRepository.findById(communeId)
                .orElseThrow(() -> new CommuneNotFoundException(communeId));
    }

    private Employee createEmployeeFromRow(Row row) {
        String code = getCellValue(row.getCell(0));
        String name = getCellValue(row.getCell(1));
        String email = getCellValue(row.getCell(2));
        String phone = getCellValue(row.getCell(3));
        Integer age = Integer.valueOf(getCellValue(row.getCell(4)));
        Long provinceId = Long.valueOf(getCellValue(row.getCell(5)));
        Long districtId = Long.valueOf(getCellValue(row.getCell(6)));
        Long communeId = Long.valueOf(getCellValue(row.getCell(7)));

        Province province = provinceRepository.findById(provinceId).orElseThrow(() -> new ProvinceNotFoundException(provinceId));
        District district = districtRepository.findById(districtId).orElseThrow(() -> new DistrictNotFoundException(districtId));
        Commune commune = communeRepository.findById(communeId).orElseThrow(() -> new CommuneNotFoundException(communeId));


        Employee emp = new Employee();
        emp.setCode(code);
        emp.setName(name);
        emp.setEmail(email);
        emp.setPhone(phone);
        emp.setAge(age);
        emp.setProvince(province);
        emp.setDistrict(district);
        emp.setCommune(commune);

        return emp;
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
