package hu.progmasters.moovsmart.dto;

import hu.progmasters.moovsmart.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomUserForm {

    @NotNull(message = "Username cannot be empty!")
    @Size(min = 1, max = 200, message = "Name must be between 3 and 200 characters!")
    private String userName;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 1, max = 200, message = "Password must be between 3 and 200 characters!")
    private String password;

    @NotNull(message = "E-mail cannot be empty!")
    @Size(min = 1, max = 200, message = "E-mail must be between 3 and 200 characters!")
    private String eMail;

    private List<Property> propertyList;
}
