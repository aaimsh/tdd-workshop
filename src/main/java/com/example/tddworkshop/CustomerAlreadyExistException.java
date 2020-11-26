package com.example.tddworkshop;

import lombok.Data;

@Data
public class CustomerAlreadyExistException extends Exception{
    private final String message = "Customer with same id already exists";
    private final String code = "4003";
    private static final long serialVersionUID = 2L;
}
