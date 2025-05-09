package com.globits.da.dto.response;

public class CertificateTypeResponseDto {
    private Long id;
    private String type;
    private String description;

    public CertificateTypeResponseDto(Long id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public CertificateTypeResponseDto() {
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
