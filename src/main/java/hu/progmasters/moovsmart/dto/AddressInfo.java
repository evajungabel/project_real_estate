package hu.progmasters.moovsmart.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfo {

    private Integer zipcode;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private Integer doorNumber;
    private String propertyName;
}
