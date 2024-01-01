package com.realestate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressForm {

    @NotNull(message = "zipcode canot be null!")
    @Positive
    private Integer zipcode;

    @NotBlank(message = "country canot be empty!")
    private String country;

    @NotBlank(message = "city canot be empty!")
    private String city;

    @NotBlank(message = "street canot be empty!")
    private String street;

    @NotBlank(message = "houseNumber canot be empty!")
    private String houseNumber;

    private Integer doorNumber;

    @NotNull(message = "propertyId cannot be null!")
    @Positive
    private Long propertyId;
}
