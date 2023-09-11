package hu.progmasters.moovsmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomUserForm {

    @NotBlank(message = "Name cannot be empty!")
    @Size(min = 1, max = 200, message = "Name must be between 3 and 200 characters!")
    private String name;

    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 1, max = 200, message = "Name must be between 3 and 200 characters!")
    private String username;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 1, max = 200, message = "Password must be between 3 and 200 characters!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")
    private String password;

    @NotNull(message = "E-mail cannot be empty!")
    @Size(min = 1, max = 200, message = "E-mail must be between 3 and 200 characters!")
    @Email
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    //VALID
    private Boolean isAgent;
}
