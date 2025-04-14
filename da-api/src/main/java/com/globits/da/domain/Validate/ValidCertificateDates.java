package com.globits.da.domain.Validate;

import javax.validation.Payload;

public @interface ValidCertificateDates {
    String message() default "Ngày cấp phải trước ngày hết hạn";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
