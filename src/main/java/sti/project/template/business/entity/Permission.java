package sti.project.template.business.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sti.project.template.base.entity.BaseEntity;

@Entity
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {
    @Column(name = "name", length = 50, nullable = false, unique = true)
    String name;

    @Column(name = "description")
    String description;
}