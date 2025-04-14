package com.globits.da.dto.request;

import com.globits.da.domain.Validate.UniqueEmployeeCode;
import com.globits.da.domain.Validate.ValidEmployeeLocation;

import javax.validation.constraints.*;
import java.util.List;

@ValidEmployeeLocation
public class EmployeeRequestDto {
    @NotBlank(message = "Code không được để trống")
    @Size(min = 6,max = 10,message = "Code phải có độ dài từ 6 đến 10 ký tự")
    @Pattern(regexp = "^\\S+$",message = "Code không được chứa khoảng trống")
    @UniqueEmployeeCode(message = "Code đã tồn tại")
    private String code;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp ="^[0-9]+$",message = "Số điện thoại phải chứa số")
    @Size(min = 10,max = 10,message = "Số điện thoại gồm 10 số")
    private String phone;

    @Min(value = 0,message = "Tuổi không được để âm")
    private Integer age;


    private Long provinceId;
    private Long districtId;
    private Long communeId;

    List<Long> certificateIds;

    public List<Long> getCertificateIds() {
        return certificateIds;
    }

    public void setCertificateIds(List<Long> certificateIds) {
        this.certificateIds = certificateIds;
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

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getCommuneId() {
        return communeId;
    }

    public void setCommuneId(Long communeId) {
        this.communeId = communeId;
    }
}
