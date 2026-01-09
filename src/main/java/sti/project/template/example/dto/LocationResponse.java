package sti.project.template.example.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sti.project.template.base.dto.BaseResponseDTO;

/**
 * Location response DTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationResponse extends BaseResponseDTO {
    private String name;
    private String code;
    private String description;
}
