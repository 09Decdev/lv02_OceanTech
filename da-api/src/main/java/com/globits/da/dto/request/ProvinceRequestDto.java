package com.globits.da.dto.request;

import java.util.List;

public class ProvinceRequestDto {
    private Long id;
  private String code;
  private String name;
  private List<DistrictRequestDto> districtId;


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

    public List<DistrictRequestDto> getDistrictId() {
        return districtId;
    }

    public void setDistrictId(List<DistrictRequestDto> districtId) {
        this.districtId = districtId;
    }
}
