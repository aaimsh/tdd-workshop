package com.example.tddworkshop;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CreditLimitRequiredFieldsValidator implements ConstraintValidator<CreditLimitRequiredFields, CreditLimit> {
    public void initialize(CreditLimitRequiredFields constraint) {
    }

    public boolean isValid(CreditLimit obj, ConstraintValidatorContext context){
        List<String> missingFields = new ArrayList<>();
        if (StringUtils.isEmpty(obj.getIdType()))
            missingFields.add("idType");
        if (StringUtils.isEmpty(obj.getNationalId()))
            missingFields.add("nationalId");
        if (missingFields.isEmpty())
            return true;
        throw new RequiredFieldsMissingException(missingFields);
    }
}
