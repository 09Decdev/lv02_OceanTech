package com.globits.da.rest;

import com.globits.da.domain.entity.Employee;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.dto.response.ImportError;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
public class RestEmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    public RestEmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/{pageIndex}/{pageSize}")
    public ApiResponse<Page<EmployeeResponseDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
        ApiResponse<Page<EmployeeResponseDto>> result = new ApiResponse<>();
        Page<EmployeeResponseDto> employeeResponseDtoPage = employeeService.getPage(pageSize, pageIndex);
        result.setData(employeeResponseDtoPage);
        result.setMessage("success");

        return result;
    }

    @PostMapping("/create")
    public ApiResponse<EmployeeResponseDto> saveEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        ApiResponse<EmployeeResponseDto> result = new ApiResponse<>();
        result.setData(employeeService.saveEmployee(dto));
        result.setMessage("Success");

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployeeResponseDto> updateEmployee(@Valid @RequestBody EmployeeRequestDto dto, @PathVariable Long id) {
        ApiResponse<EmployeeResponseDto> result = new ApiResponse<>();
        result.setData(employeeService.updateEmployee(id, dto));
        result.setMessage("Successfully updated");

        return result;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteEmployee(@PathVariable Long id) {
        ApiResponse<Boolean> result = new ApiResponse<>();
        Boolean deleteEmployee = employeeService.deleteEmployee(id);
        result.setData(deleteEmployee);
        result.setMessage("Successfully deleted");

        return result;
    }

    @PostMapping("/{employeeId}/certificates")
    public ApiResponse<EmployeeResponseDto> addCertificatesToEmployee(
            @PathVariable Long employeeId,
            @RequestBody List<Long> certificateIds) {
        ApiResponse<EmployeeResponseDto> result = new ApiResponse<>();
        result.setData(employeeService.addCertificate(employeeId, certificateIds));
        return result;
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployeeResponseDto> getEmployee(@PathVariable Long id) {
        ApiResponse<EmployeeResponseDto> result = new ApiResponse<>();
        result.setData(employeeService.getEmployee(id));
        return result;
    }

    @GetMapping
    public ApiResponse<List<EmployeeResponseDto>> getEmployees() {
        ApiResponse<List<EmployeeResponseDto>> result = new ApiResponse<>();
        result.setData(employeeService.getEmployees());
        return result;
    }

    @PostMapping("/import-employees")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        List<ImportError> errors = employeeService.importEmployeesFromExcel(file);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok("Import thành công!");
    }

}
