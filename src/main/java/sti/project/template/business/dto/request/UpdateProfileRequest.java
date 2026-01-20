package sti.project.template.business.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {

    @Size(max = 255, message = "{validation.name.size}")
    String name;

    @Size(max = 50, message = "{validation.phone.size}")
    String phone;

    String avatar;

    String address;

    LocalDate dob;

    Map<String, Object> settings;
}
