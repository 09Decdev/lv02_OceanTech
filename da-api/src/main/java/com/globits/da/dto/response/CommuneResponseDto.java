package com.globits.da.dto.response;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class CommuneResponseDto {
    private Long id;
    private String code;
    private String name;
    private Long districtId;

    public CommuneResponseDto() {
    }

    public CommuneResponseDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
