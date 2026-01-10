package sti.project.template.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
}
