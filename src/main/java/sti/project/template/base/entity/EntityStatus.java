package sti.project.template.base.entity;

import jakarta.persistence.Converter;
import sti.project.template.base.enums.PersistableEnum;

public enum EntityStatus implements PersistableEnum<Integer> {
    ACTIVE(1),
    INACTIVE(2),
    DELETED(3),
    ORPHANED(4);

    private final int code;

    EntityStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Converter(autoApply = true)
    public static class EntityStatusConverter extends EnumConverter<EntityStatus> {
        public EntityStatusConverter() {
            super(EntityStatus.class);
        }
    }
}