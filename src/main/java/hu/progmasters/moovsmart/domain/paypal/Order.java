package hu.progmasters.moovsmart.domain.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private double totalAmount;
    private String currency;
    private String description;
}
