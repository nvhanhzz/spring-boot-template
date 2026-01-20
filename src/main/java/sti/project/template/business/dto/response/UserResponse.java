package sti.project.template.business.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import sti.project.template.base.dto.BaseResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends BaseResponseDTO {
    String name;
    String email;
    String phone;
    String avatar;
    String address;
    LocalDate dob;
    List<RoleResponse> roles;
    Map<String, Object> settings;
}
