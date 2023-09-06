package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfoForProperty {
    private Integer zipcode;
    private String country;
    private String city;
}
