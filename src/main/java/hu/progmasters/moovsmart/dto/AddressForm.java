package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressForm {

    @NotBlank(message = "zipcode canot be empty!")
    private Integer zipcode;

    @NotBlank(message = "country canot be empty!")
    private String country;

    @NotBlank(message = "city canot be empty!")
    private String city;

    @NotBlank(message = "street canot be empty!")
    private String street;

    @NotBlank(message = "houseNumber canot be empty!")
    private Integer houseNumber;

    private Integer doorNumber;

    @NotBlank(message = "propertyId canot be empty!")
    private Long propertyId;
}
