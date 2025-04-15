package com.globits.da.rest;

import com.globits.da.Mapper.EmployeeMapper;
import com.globits.da.domain.entity.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.request.EmployeeRequestDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.EmployeeResponseDto;
import com.globits.da.dto.response.ImportError;
import com.globits.da.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class RestEmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("/{pageIndex}/{pageSize}")
    public ApiResponse<Page<EmployeeResponseDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize){
        ApiResponse<Page<EmployeeResponseDto>> result=new ApiResponse<>();
        Page<EmployeeResponseDto>employeeResponseDtoPage=employeeService.getPage(pageSize,pageIndex);
        result.setData(employeeResponseDtoPage);
        result.setMessage("success");

        return result;
    }

    @PostMapping("/create")
    public ApiResponse<EmployeeResponseDto> saveEmployee( @Valid @RequestBody EmployeeRequestDto dto){
        ApiResponse<EmployeeResponseDto>result=new ApiResponse<>();
        Employee employee = employeeMapper.toEntity(dto);
        employee = employeeService.save(employee);
        result.setData(employeeMapper.toDto(employee));
        result.setMessage("Success");

        return result;
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployeeResponseDto>updateEmployee(@Valid @RequestBody EmployeeRequestDto dto, @PathVariable Long id){
        ApiResponse<EmployeeResponseDto>result=new ApiResponse<>();
        Employee employee = employeeService.updateEmployee(id,dto);
        result.setData(employeeMapper.toDto(employee));
        result.setMessage("Successfully updated");

        return result;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean>deleteEmployee(@PathVariable Long id){
        ApiResponse<Boolean>result=new ApiResponse<>();
        Boolean deleteEmployee=employeeService.deleteEmployee(id);
        result.setData(deleteEmployee);
        result.setMessage("Successfully deleted");

        return result;
    }
    @PostMapping("/{employeeId}/certificates")
    public ApiResponse<EmployeeResponseDto> addCertificatesToEmployee(
            @PathVariable Long employeeId,
            @RequestBody List<Long> certificateIds) {
        ApiResponse<EmployeeResponseDto>result=new ApiResponse<>();
        result.setData(employeeService.addCertificate(employeeId, certificateIds));
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
