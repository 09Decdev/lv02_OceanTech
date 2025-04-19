package com.globits.da.dto.request;

import javax.validation.constraints.NotBlank;

public class CertificateTypeRequestDto {
    private Long id;
    @NotBlank(message = "Không được để trống")
    private String type;
    private String description;

    public CertificateTypeRequestDto(Long id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public CertificateTypeRequestDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
