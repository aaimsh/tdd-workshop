package com.example.tddworkshop;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.*;

@RestController
@AllArgsConstructor
public class CreditLimitController {

    CreditLimitService creditLimitService;


    @GetMapping("credit-limit/{nationalId}")
    ResponseEntity getCreditLimit(@PathVariable String nationalId) throws CustomerNotFoundException {
        CreditLimit creditLimit = creditLimitService.getCustomerCreditLimit(nationalId);
        return ok(creditLimit);
    }

    @ExceptionHandler
    ResponseEntity handleError(CustomerNotFoundException e){
        return status(HttpStatus.NOT_FOUND).body(Map.of(
                "code", e.getCode(),
                "message", e.getMessage()
                )
        );
    }

    @PostMapping("credit-limit")
    ResponseEntity createCreditLimit(@RequestBody @Valid CreditLimit creditLimit) throws CustomerAlreadyExistException {
        creditLimitService.createCreditLimit(creditLimit);
        return status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler
    ResponseEntity handleError(RequiredFieldsMissingException exception){
        return status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", "Missing fields: ".concat(exception.getMissingFields().toString()),
                "code", "4001"
                )
        );
    }

    @ExceptionHandler
    ResponseEntity handleError(CustomerAlreadyExistException e){
        return status(HttpStatus.BAD_REQUEST).body(Map.of(
                "code", e.getCode(),
                "message", e.getMessage()
                )
        );
    }
}
