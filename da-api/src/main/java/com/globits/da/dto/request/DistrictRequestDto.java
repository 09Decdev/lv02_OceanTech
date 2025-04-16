package com.globits.da.dto.request;

//import com.globits.da.domain.entity.Commune;

import java.util.List;

public class DistrictRequestDto {
    private Long id;
    private String name;
    private String code;
    private Long provinceId;
    private List<CommuneRequestDto> communeId;

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CommuneRequestDto> getCommuneId() {
        return communeId;
    }

    public void setCommuneId(List<CommuneRequestDto> communeId) {
        this.communeId = communeId;
    }
}
