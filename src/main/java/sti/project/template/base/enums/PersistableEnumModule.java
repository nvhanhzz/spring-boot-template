package sti.project.template.base.enums;

import com.fasterxml.jackson.databind.module.SimpleModule;
import sti.project.scada.base.entity.EntityStatus;
import sti.project.scada.business.enums.*;

public class PersistableEnumModule extends SimpleModule {

    public PersistableEnumModule() {
        super("PersistableEnumModule");

        addDeserializer(EntityStatus.class, new PersistableEnumDeserializer<>(EntityStatus.class));
        addDeserializer(ActionType.class, new PersistableEnumDeserializer<>(ActionType.class));
        addDeserializer(AssetType.class, new PersistableEnumDeserializer<>(AssetType.class));
        addDeserializer(CommandStatus.class, new PersistableEnumDeserializer<>(CommandStatus.class));
        addDeserializer(CommandType.class, new PersistableEnumDeserializer<>(CommandType.class));
        addDeserializer(ConditionType.class, new PersistableEnumDeserializer<>(ConditionType.class));
        addDeserializer(DataType.class, new PersistableEnumDeserializer<>(DataType.class));
        addDeserializer(EventSeverity.class, new PersistableEnumDeserializer<>(EventSeverity.class));
        addDeserializer(EventStatus.class, new PersistableEnumDeserializer<>(EventStatus.class));
        addDeserializer(MachineStatus.class, new PersistableEnumDeserializer<>(MachineStatus.class));
        addDeserializer(ProtocolType.class, new PersistableEnumDeserializer<>(ProtocolType.class));
        addDeserializer(TransformType.class, new PersistableEnumDeserializer<>(TransformType.class));
    }
}
