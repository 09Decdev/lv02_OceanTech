package com.globits.da.dto.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommuneRequestDto {
    private Long id;
    @NotBlank(message = "Code không được để trống")
    private String code;

    @NotBlank(message = "Name không được để trống")
    private String name;

    @NotNull(message = "DistrictId không được để trống")
    private Long districtId;


    private Long employeeId;

    public CommuneRequestDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
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

}
