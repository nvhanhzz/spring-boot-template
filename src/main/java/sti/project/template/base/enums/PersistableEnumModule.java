package sti.project.template.base.enums;

import com.fasterxml.jackson.databind.module.SimpleModule;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.business.enums.*;

public class PersistableEnumModule extends SimpleModule {

    public PersistableEnumModule() {
        super("PersistableEnumModule");

        addDeserializer(EntityStatus.class, new PersistableEnumDeserializer<>(EntityStatus.class));
        addDeserializer(AssetType.class, new PersistableEnumDeserializer<>(AssetType.class));

        // ADD ENUM HERE
    }
}