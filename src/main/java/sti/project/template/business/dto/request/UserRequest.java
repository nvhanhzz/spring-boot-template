package sti.project.template.business.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @NotBlank(message = "{validation.name.required}")
    @Size(max = 255, message = "{validation.name.size}")
    String name;

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email.invalid}")
    @Size(max = 50, message = "{validation.email.size}")
    String email;

    @NotBlank(message = "{validation.password.required}")
    @Size(min = 6, max = 255, message = "{validation.password.size}")
    String password;

    @Size(max = 50, message = "{validation.phone.size}")
    String phone;

    String avatar;

    String address;

    LocalDate dob;

    List<UUID> roleIds;
}
