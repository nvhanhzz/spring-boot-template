package sti.project.template.business.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Converter;
import sti.project.scada.base.enums.PersistableEnum;

@Schema(enumAsRef = true, description = "Types of visual assets")
public enum AssetType implements PersistableEnum<Integer> {
    IMAGE(1),
    ICON(2),
    ANIMATION(3);

    private final int code;

    AssetType(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Converter(autoApply = true)
    public static class AssetTypeConverter extends PersistableEnum.EnumConverter<AssetType> {
        public AssetTypeConverter() {
            super(AssetType.class);
        }
    }
}
