package com.polovyi.ivan.tutorials.entity;

import com.polovyi.ivan.tutorials.dto.CreateCustomerRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class CustomerEntity {

    @Id
    private Long id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private LocalDate createdAt;

    public static CustomerEntity valueOf(CreateCustomerRequest createCustomerRequest) {
        return builder()
                .id(createCustomerRequest.getId())
                .fullName(createCustomerRequest.getFullName())
                .phoneNumber(createCustomerRequest.getPhoneNumber())
                .address(createCustomerRequest.getAddress())
                .createdAt(LocalDate.now())
                .build();
    }
}