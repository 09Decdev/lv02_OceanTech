package com.globits.da.dto.request;

import com.globits.da.domain.Validate.ValidCertificateDates;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@ValidCertificateDates
public class CertificateRequestDto {
    @NotBlank(message = "Không được để trống teen văn bằng")
    private String name;

    @NotBlank(message = "Không được để trống đơn vị cấp")
    private String issuingOrganization;

    @NotNull(message = "Không được để trống ngày cấp")
    @PastOrPresent(message=" Ngày cấp không được là ngày ở tương lai" )// Tổ chức cấp
    private LocalDate issueDate;

    @NotNull(message = "Không được để trống ngày hết hạn")
    @FutureOrPresent(message = "Ngày hết hạn phải ở hiện tại hoặc tương lai")
    private LocalDate expiryDate;

    private Long provinceId;




    public CertificateRequestDto(String name, LocalDate expiryDate, LocalDate issueDate, String issuingOrganization) {
    }

    public CertificateRequestDto() {
    }

    //    private Long employeeId;



    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
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
}
