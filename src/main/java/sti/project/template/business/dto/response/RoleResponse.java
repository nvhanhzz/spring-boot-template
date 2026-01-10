package sti.project.template.business.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sti.project.template.base.dto.BaseResponseDTO;

import java.util.Set;

/**
 * Role response DTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleResponse extends BaseResponseDTO {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
