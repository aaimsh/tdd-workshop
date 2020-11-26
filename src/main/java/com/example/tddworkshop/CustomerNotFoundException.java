package com.example.tddworkshop;

import lombok.Data;

@Data

public class CustomerNotFoundException extends Exception{
    private final String message = "Customer does not exist";
    private final String code = "4041";
    private static final long serialVersionUID = 1L;

}
