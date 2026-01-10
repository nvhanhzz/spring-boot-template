package sti.project.template.business.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.service.impl.BaseServiceImpl;
import sti.project.template.business.dto.request.UserRequest;
import sti.project.template.business.dto.response.UserResponse;
import sti.project.template.business.entity.Role;
import sti.project.template.business.entity.User;
import sti.project.template.business.mapper.UserMapper;
import sti.project.template.business.repository.RoleRepository;
import sti.project.template.business.repository.UserRepository;
import sti.project.template.business.service.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserResponse, UserRequest>
        implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, UserMapper mapper,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(repository, mapper, User.class);
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name", "email" };
    }

    @Override
    protected void beforeCreate(User entity, UserRequest request) {
        validateEmailUnique(request.getEmail(), null);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        assignRoles(entity, request.getRoleIds());
    }

    @Override
    protected void beforeUpdate(User entity, UserRequest request) {
        if (!entity.getEmail().equals(request.getEmail())) {
            validateEmailUnique(request.getEmail(), entity.getEmail());
        }
        // Update password only if provided and different
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        assignRoles(entity, request.getRoleIds());
    }

    private void validateEmailUnique(String email, String currentEmail) {
        if (userRepository.existsByEmailAndStatusNot(email, EntityStatus.DELETED)) {
            if (currentEmail == null || !currentEmail.equals(email)) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
        }
    }

    private void assignRoles(User user, Set<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            user.setRoles(new HashSet<>());
            return;
        }

        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            Role role = roleRepository.findByIdAndStatusNot(roleId, EntityStatus.DELETED)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            roles.add(role);
        }
        user.setRoles(roles);
    }
}
