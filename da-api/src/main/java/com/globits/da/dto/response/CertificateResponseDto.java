package com.globits.da.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.da.domain.entity.Certificate;

import java.time.LocalDate;

public class CertificateResponseDto {
    private Long id;
    private String name;
    private String issuingOrganization;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private ProvinceResponseDto provinceDto;

    private CertificateTypeResponseDto certificateTypeRespon;

    public CertificateResponseDto() {

    }

    public CertificateResponseDto(Certificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.issuingOrganization = certificate.getIssuingOrganization();
        this.issueDate = certificate.getIssueDate();
        this.expiryDate = certificate.getExpiryDate();
    }

    public ProvinceResponseDto getProvinceDto() {
        return provinceDto;
    }

    public void setProvinceDto(ProvinceResponseDto provinceDto) {
        this.provinceDto = provinceDto;
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

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CertificateTypeResponseDto getCertificateTypeRespon() {
        return certificateTypeRespon;
    }

    public void setCertificateTypeRespon(CertificateTypeResponseDto certificateTypeRespon) {
        this.certificateTypeRespon = certificateTypeRespon;
    }
}
