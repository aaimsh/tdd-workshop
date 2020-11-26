package com.example.tddworkshop;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@CreditLimitRequiredFields
public class CreditLimit {
    private String nationalId;
    private String idType;
    private String customerName;
    private Integer creditLimit;

}
