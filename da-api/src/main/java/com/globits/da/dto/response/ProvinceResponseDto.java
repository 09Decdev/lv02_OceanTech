package com.globits.da.dto.response;

import com.globits.da.domain.entity.Province;

import java.util.List;
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class ProvinceResponseDto {
    private Long id;
    private String code;
    private String name;
    private List<DistrictResponseDto> districts;

    public ProvinceResponseDto() {
    }
    public ProvinceResponseDto(Long id, String code, String name, List<DistrictResponseDto> districts) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.districts = districts;
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

    public List<DistrictResponseDto> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DistrictResponseDto> districts) {
        this.districts = districts;
    }
}