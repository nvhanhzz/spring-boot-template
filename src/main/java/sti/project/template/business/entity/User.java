package sti.project.template.business.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import sti.project.template.base.entity.BaseEntity;

import java.time.LocalDate;
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
}