package sti.project.template.business.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sti.project.template.base.dto.BaseResponseDTO;

/**
 * Example response DTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExampleResponse extends BaseResponseDTO {
    private String name;
    private String code;
    private String description;
}
