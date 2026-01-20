package sti.project.template.business.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sti.project.template.base.dto.PageDTO;
import sti.project.template.base.dto.SearchCriteria;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.file.FileService;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl extends BaseServiceImpl<User, UserResponse, UserRequest>
        implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    FileService fileService;

    ThreadLocal<String> oldImageHolder = new ThreadLocal<>();

    public UserServiceImpl(UserRepository repository, UserMapper mapper,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder, FileService fileService) {
        super(repository, mapper, User.class);
        this.userRepository = repository;
        this.userMapper = mapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name", "email" };
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<UserResponse> search(SearchCriteria criteria) {
        Sort sort = Sort.by(Sort.Direction.fromString(criteria.getSortDir()), criteria.getSortBy());
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        Specification<User> spec = buildSearchSpecification(criteria);
        Page<User> pageResult = userRepository.findAll(spec, pageable);

        if (pageResult.isEmpty()) {
            return PageDTO.of(List.of(), 0L);
        }
        List<UUID> ids = pageResult.getContent().stream()
                .map(User::getId)
                .toList();
        List<User> users = userRepository.findByIdsWithRoles(ids);

        return PageDTO.of(userMapper.toResponseList(users), pageResult.getTotalElements());
    }

    @Override
    protected void beforeCreate(User entity, UserRequest request) {
        validateEmailUnique(request.getEmail(), null);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        assignRoles(entity, request.getRoleIds());
    }

    @Override
    protected void afterCreate(User entity, UserRequest request) {
        fileService.markAsUsed(request.getAvatar());
    }

    @Override
    protected void beforeUpdate(User entity, UserRequest request) {
        if (!entity.getEmail().equals(request.getEmail())) {
            validateEmailUnique(request.getEmail(), entity.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        assignRoles(entity, request.getRoleIds());

        handleAvatarUpdate(entity, request);
    }

    @Override
    protected void afterUpdate(User entity, UserRequest request) {
        String oldImage = oldImageHolder.get();
        oldImageHolder.remove();
        if (oldImage != null) {
            fileService.deleteFiles(List.of(oldImage));
        }
    }

    private void handleAvatarUpdate(User entity, UserRequest request) {
        String oldImage = entity.getAvatar();
        String newImage = request.getAvatar();

        if (newImage != null && !newImage.equals(oldImage)) {
            fileService.markAsUsed(newImage);
            oldImageHolder.set(oldImage);
        }
    }

    private void validateEmailUnique(String email, String currentEmail) {
        if (userRepository.existsByEmailAndStatusNot(email, EntityStatus.DELETED)) {
            if (currentEmail == null || !currentEmail.equals(email)) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
        }
    }

    private void assignRoles(User user, List<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            user.setRoles(new HashSet<>());
            return;
        }

        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            Role role = roleRepository.findByIdAndStatusNot(roleId, EntityStatus.DELETED)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Role"));
            roles.add(role);
        }
        user.setRoles(roles);
    }
}