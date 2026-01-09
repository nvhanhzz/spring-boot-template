package sti.project.template.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Example request DTO for create/update operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    private String code;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
}
