package com.example.tddworkshop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class RequiredFieldsMissingException extends IllegalArgumentException {
    private final List<String> missingFields;
}
