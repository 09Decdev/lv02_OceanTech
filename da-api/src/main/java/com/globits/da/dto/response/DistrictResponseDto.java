package com.globits.da.dto.response;

import java.util.List;

public class DistrictResponseDto {
    private Long id;
    private String code;
    private String name;
    private List<CommuneResponseDto> communes;

    public DistrictResponseDto() {
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

    public List<CommuneResponseDto> getCommunes() {
        return communes;
    }

    public void setCommunes(List<CommuneResponseDto> communes) {
        this.communes = communes;
    }
}
