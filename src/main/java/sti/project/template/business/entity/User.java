package sti.project.template.business.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import sti.project.template.base.entity.BaseEntity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "phone", length = 50)
    String phone;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "address")
    String address;

    @Column(name = "dob")
    LocalDate dob;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles;

    @Type(JsonType.class)
    @Column(name = "settings", columnDefinition = "jsonb")
    Map<String, Object> settings;
}