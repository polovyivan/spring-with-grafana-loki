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
public class PartiallyUpdateCustomerRequest {

    @NotNull
    private String phoneNumber;

}
