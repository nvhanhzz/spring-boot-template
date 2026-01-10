package sti.project.template.base.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.business.entity.Role;
import sti.project.template.business.entity.User;
import sti.project.template.business.repository.RoleRepository;
import sti.project.template.business.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Initializes default roles and users on application startup.
 * Configuration is loaded from application YAML.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final InitializerProperties initializerProperties;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (!initializerProperties.isEnabled()) {
            log.info("Data initializer is disabled");
            return;
        }

        log.info("Starting data initialization...");
        initializeRoles();
        initializeUsers();
        log.info("Data initialization completed");
    }

    private void initializeRoles() {
        List<InitializerProperties.RoleConfig> roleConfigs = initializerProperties.getRoles();
        if (roleConfigs == null || roleConfigs.isEmpty()) {
            log.info("No roles configured for initialization");
            return;
        }

        for (InitializerProperties.RoleConfig roleConfig : roleConfigs) {
            if (roleRepository.existsByNameAndStatusNot(roleConfig.getName(), EntityStatus.DELETED)) {
                log.debug("Role '{}' already exists, skipping", roleConfig.getName());
                continue;
            }

            Role role = new Role();
            role.setName(roleConfig.getName());
            role.setDescription(roleConfig.getDescription());
            roleRepository.save(role);
            log.info("Created role: {}", roleConfig.getName());
        }
    }

    private void initializeUsers() {
        List<InitializerProperties.UserConfig> userConfigs = initializerProperties.getUsers();
        if (userConfigs == null || userConfigs.isEmpty()) {
            log.info("No users configured for initialization");
            return;
        }

        // Pre-load all roles to avoid N+1 queries
        Map<String, Role> rolesMap = loadRolesMap();

        for (InitializerProperties.UserConfig userConfig : userConfigs) {
            if (userRepository.existsByEmailAndStatusNot(userConfig.getEmail(), EntityStatus.DELETED)) {
                log.debug("User '{}' already exists, skipping", userConfig.getEmail());
                continue;
            }

            User user = new User();
            user.setName(userConfig.getName());
            user.setEmail(userConfig.getEmail());
            user.setPassword(passwordEncoder.encode(userConfig.getPassword()));
            user.setPhone(userConfig.getPhone());

            // Assign roles
            Set<Role> userRoles = resolveRoles(userConfig.getRoles(), rolesMap);
            user.setRoles(userRoles);

            userRepository.save(user);
            log.info("Created user: {} with roles: {}", userConfig.getEmail(),
                    userRoles.stream().map(Role::getName).collect(Collectors.joining(", ")));
        }
    }

    private Map<String, Role> loadRolesMap() {
        return roleRepository.findAll().stream()
                .filter(role -> role.getStatus() != EntityStatus.DELETED)
                .collect(Collectors.toMap(Role::getName, role -> role, (a, b) -> a));
    }

    private Set<Role> resolveRoles(List<String> roleNames, Map<String, Role> rolesMap) {
        if (roleNames == null || roleNames.isEmpty()) {
            return new HashSet<>();
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = rolesMap.get(roleName);
            if (role != null) {
                roles.add(role);
            } else {
                log.warn("Role '{}' not found, skipping role assignment", roleName);
            }
        }
        return roles;
    }
}
