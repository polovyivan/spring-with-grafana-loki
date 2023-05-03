package com.polovyi.ivan.tutorials.controller;

import com.polovyi.ivan.tutorials.dto.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.CustomerResponse;
import com.polovyi.ivan.tutorials.dto.PartiallyUpdateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.UpdateCustomerRequest;
import com.polovyi.ivan.tutorials.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping(path = "/v1/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "/v1/customers-with-filters")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAllCustomersWithFilters(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdAt) {
        return customerService.getCustomersWithFilters(fullName, phoneNumber, createdAt);
    }

    @GetMapping(path = "/v1/customers/{customerId}")
    public CustomerResponse updateCustomer(@PathVariable Long customerId) {
       return customerService.getCustomersById(customerId);
    }


    @PostMapping(path = "/v1/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest,
            UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        Long customerId = customerService.createCustomer(createCustomerRequest).getId();
        response.addHeader("location", uriBuilder.path("/v1/customers/{id}").buildAndExpand(customerId).toUriString());
    }

    @PutMapping(path = "/v1/customers/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(@PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        customerService.updateCustomer(customerId, updateCustomerRequest);
    }

    @PatchMapping(path = "/v1/customers/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void partiallyUpdateCustomer(@PathVariable Long customerId,
            @Valid @RequestBody PartiallyUpdateCustomerRequest partiallyUpdateCustomerRequest) {
        customerService.partiallyUpdateCustomer(customerId, partiallyUpdateCustomerRequest);
    }

    @DeleteMapping(path = "/v1/customers/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
    }

}
