package com.globits.da.dto.response;

import com.globits.da.domain.entity.Employee;

import javax.validation.constraints.*;
import java.util.List;

public class EmployeeResponseDto {
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private Integer age;
    private String districtName;
    private String provinceName;
    private String communeName;
    private List<CertificateResponseDto> certificates;

    public EmployeeResponseDto(Employee employee){
        if (employee!=null){
            this.id=employee.getId();
            this.code=employee.getCode();
            this.name=employee.getName();
            this.email=employee.getEmail();
            this.phone=employee.getPhone();
            this.age=employee.getAge();
            this.communeName=employee.getCommune().getName();
            this.districtName=employee.getDistrict().getName();
            this.provinceName=employee.getProvince().getName();
        }
    }

    public EmployeeResponseDto() {
    }

    public List<CertificateResponseDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateResponseDto> certificates) {
        this.certificates = certificates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }
}
