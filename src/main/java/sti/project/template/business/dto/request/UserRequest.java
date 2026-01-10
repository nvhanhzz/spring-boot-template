package sti.project.template.business.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email must be at most 50 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @Size(max = 50, message = "Phone must be at most 50 characters")
    private String phone;

    private String avatar;

    private String address;

    private LocalDate dob;

    private Set<UUID> roleIds;
}
