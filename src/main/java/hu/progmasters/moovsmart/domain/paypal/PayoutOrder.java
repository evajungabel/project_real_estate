package hu.progmasters.moovsmart.domain.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayoutOrder {
    private double totalAmount;
    private String currency;
    private String receiver;
}
