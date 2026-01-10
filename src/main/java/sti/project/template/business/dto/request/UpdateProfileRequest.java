package sti.project.template.business.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @Size(max = 50, message = "Phone must be at most 50 characters")
    private String phone;

    private String avatar;

    private String address;

    private LocalDate dob;
}
