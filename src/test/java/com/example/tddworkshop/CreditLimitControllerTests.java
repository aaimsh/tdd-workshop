package com.example.tddworkshop;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.internal.bytebuddy.matcher.StringMatcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

@WebMvcTest(CreditLimitController.class)
public class CreditLimitControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CreditLimitService creditLimitService;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET /credit-limit tests
     */
    @Test
    void getCreditLimitShouldReturnCreditLimit() throws Exception {
        CreditLimit mockCreditLimit = new CreditLimit("1234567890", "S", "Abdulailah", 123456);

        when(creditLimitService.getCustomerCreditLimit(anyString())).thenReturn(mockCreditLimit);
        mockMvc.perform(get("/credit-limit/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nationalId").value(mockCreditLimit.getNationalId()))
                .andExpect(jsonPath("idType").value(mockCreditLimit.getIdType()))
                .andExpect(jsonPath("customerName").value(mockCreditLimit.getCustomerName()))
                .andExpect(jsonPath("creditLimit").value(mockCreditLimit.getCreditLimit()));
//
}

    @Test
    void whenServiceThrowsCustomerNotFoundGetCreditLimitShouldReturn404NotFound() throws Exception{
        doThrow(CustomerNotFoundException.class).when(creditLimitService).getCustomerCreditLimit(anyString());
        mockMvc.perform(get("/credit-limit/1234567890"))
                .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value("4041"))
        .andExpect(jsonPath("message").exists());

    }

    /**
     * POST /credit-limit tests
     */
    @Test
    void whenCallingCreateCreditLimitWithoutIdShouldThrow400BadRequest() throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(Map.of("idType","S"));
        mockMvc
                .perform(post("/credit-limit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBytes))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("4001"))
                .andExpect(jsonPath("message").value(StringContains.containsString("nationalId")));
    }

    @Test
    void whenCallingCreateCreditLimitWithoutIdTypeShouldThrow400BadRequest() throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(Map.of("nationalId","1087855068"));
        mockMvc
                .perform(post("/credit-limit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBytes))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("4001"))
                .andExpect(jsonPath("message").value(StringContains.containsString("idType")));
    }

    @Test
    void whenCallingCreateCreditLimitWithoutRequiredFieldsShouldThrow400BadRequest() throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(Map.of("customerName","test"));
        mockMvc
                .perform(post("/credit-limit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBytes))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("4001"))
                .andExpect(jsonPath("message").value(StringContains.containsString("idType, nationalId")));
    }

    @Test
    void whenServiceThrowsCustomerAlreadyExistPostCreditLimitShouldReturn400BadRequest() throws Exception{
        doThrow(CustomerAlreadyExistException.class).when(creditLimitService).createCreditLimit(any(CreditLimit.class));
        byte[] jsonBytes = objectMapper.writeValueAsBytes(new CreditLimit("1234567890", "S", "Abdulailah", 123456));
        mockMvc
                .perform(post("/credit-limit/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBytes))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("4003"))
                .andExpect(jsonPath("message").exists());

    }



}
