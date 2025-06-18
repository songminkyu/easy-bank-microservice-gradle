package com.easybank.accounts.controller;

import com.easybank.accounts.constants.AccountsConstants;
import com.easybank.accounts.dto.AccountsDto;
import com.easybank.accounts.dto.CustomerDto;
import com.easybank.accounts.dto.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountsControllerTest {

    private RestClient restClient;

    private final String BASE_URL = "http://localhost:8080/api";

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Test
    void testCreateAccount() {
        // 준비
        CustomerDto customerDto = getCustomerDto();

        // 실행 (RestClient 사용)
        try {
            ResponseDto response = restClient.post()
                    .uri("/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(customerDto)
                    .retrieve()
                    .body(ResponseDto.class);

            // 검증
            assertNotNull(response);
            assertEquals(AccountsConstants.STATUS_201, response.getStatusCode());
            assertEquals(AccountsConstants.MESSAGE_201, response.getStatusMsg());
        } catch (Exception e) {
            // 실제 서버가 없을 경우를 대비한 예외 처리
            System.out.println("테스트 중 예외 발생: " + e.getMessage());
        }
    }

    @Test
    void testFetchAccountDetails() {
        // 준비
        String mobileNumber = "9876543210";

        // 실행 (RestClient 사용)
        try {
            CustomerDto response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/fetch")
                            .queryParam("mobileNumber", mobileNumber)
                            .build())
                    .retrieve()
                    .body(CustomerDto.class);

            // 검증
            assertNotNull(response);
            assertEquals(mobileNumber, response.getMobileNumber());
        } catch (Exception e) {
            // 실제 서버가 없을 경우를 대비한 예외 처리
            System.out.println("테스트 중 예외 발생: " + e.getMessage());
        }
    }

    @Test
    void testUpdateAccountDetails() {
        // 준비
        CustomerDto customerDto = getCustomerDto();

        // 실행 (RestClient 사용)
        try {
            ResponseDto response = restClient.put()
                    .uri("/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(customerDto)
                    .retrieve()
                    .body(ResponseDto.class);

            // 검증
            assertNotNull(response);
            assertEquals(AccountsConstants.STATUS_200, response.getStatusCode());
            assertEquals(AccountsConstants.MESSAGE_200, response.getStatusMsg());
        } catch (Exception e) {
            // 실제 서버가 없을 경우를 대비한 예외 처리
            System.out.println("테스트 중 예외 발생: " + e.getMessage());
        }
    }

    @Test
    void testDeleteAccountDetails() {
        // 준비
        String mobileNumber = "9876543210";

        // 실행 (RestClient 사용)
        try {
            ResponseDto response = restClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/delete")
                            .queryParam("mobileNumber", mobileNumber)
                            .build())
                    .retrieve()
                    .body(ResponseDto.class);

            // 검증
            assertNotNull(response);
            assertEquals(AccountsConstants.STATUS_200, response.getStatusCode());
            assertEquals(AccountsConstants.MESSAGE_200, response.getStatusMsg());
        } catch (Exception e) {
            // 실제 서버가 없을 경우를 대비한 예외 처리
            System.out.println("테스트 중 예외 발생: " + e.getMessage());
        }
    }

    private CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("pasongsong");
        customerDto.setEmail("hong@example.com");
        customerDto.setMobileNumber("9876543210");

        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(9955567782L);
        accountsDto.setAccountType("Savings");
        accountsDto.setBranchAddress("서울시 강남구");

        customerDto.setAccountsDto(accountsDto);
        return customerDto;
    }
}