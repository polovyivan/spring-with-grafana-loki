package com.polovyi.ivan.tutorials.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @NotNull
    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String address;

}
