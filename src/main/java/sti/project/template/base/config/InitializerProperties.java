package sti.project.template.base.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for data initialization.
 * Loads initial users and roles from application YAML.
 */
@Configuration
@ConfigurationProperties(prefix = "app.initializer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class InitializerProperties {

    boolean enabled = false;
    List<RoleConfig> roles = new ArrayList<>();
    List<UserConfig> users = new ArrayList<>();

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleConfig {
        String name;
        String description;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserConfig {
        String name;
        String email;
        String password;
        String phone;
        List<String> roles = new ArrayList<>();
    }
}
