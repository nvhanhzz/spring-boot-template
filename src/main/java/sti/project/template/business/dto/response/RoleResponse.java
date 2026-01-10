package sti.project.template.business.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sti.project.template.base.dto.BaseResponseDTO;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse extends BaseResponseDTO {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
