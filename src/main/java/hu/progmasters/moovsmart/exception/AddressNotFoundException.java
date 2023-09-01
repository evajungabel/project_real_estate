package hu.progmasters.moovsmart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AddressNotFoundException extends  RuntimeException {
    private final Long propertyId;
}

