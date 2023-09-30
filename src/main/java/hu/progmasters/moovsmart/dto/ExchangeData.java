package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExchangeData {
    private Double EUR;
    private Double HUF;
    private Double USD;
    private Double CNY;
    private Double RON;
    private Double RUB;
    private Double XPF;
}
