package com.example.tddworkshop;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CreditLimitRequiredFieldsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreditLimitRequiredFields {
    String message() default "Required field missing";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
