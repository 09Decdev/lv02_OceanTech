package com.globits.da.dto.response;

import java.util.List;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class DistrictResponseDto {
    private Long id;
    private String code;
    private String name;
    private List<CommuneResponseDto> communes;

    public DistrictResponseDto() {
    }

    public DistrictResponseDto(Long id, String code, String name, List<CommuneResponseDto> communes) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.communes = communes;
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
