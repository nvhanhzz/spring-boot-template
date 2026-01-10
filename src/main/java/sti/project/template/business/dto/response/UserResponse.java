package sti.project.template.business.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sti.project.template.base.dto.BaseResponseDTO;

import java.time.LocalDate;
import java.util.Set;

/**
 * User response DTO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BaseResponseDTO {
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private LocalDate dob;
    private Set<RoleResponse> roles;
}
