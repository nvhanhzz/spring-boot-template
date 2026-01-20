package sti.project.template.base.enums;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PersistableEnum<T> {

    T getCode();

    abstract class EnumConverter<E extends Enum<E> & PersistableEnum<Integer>>
            implements AttributeConverter<E, Integer> {

        private final Map<Integer, E> codeToEnum;

        protected EnumConverter(Class<E> enumClass) {
            this.codeToEnum = Arrays.stream(enumClass.getEnumConstants())
                    .collect(Collectors.toMap(
                            PersistableEnum::getCode,
                            Function.identity()));
        }

        @Override
        public Integer convertToDatabaseColumn(E attribute) {
            return attribute != null ? attribute.getCode() : null;
        }

        @Override
        public E convertToEntityAttribute(Integer dbData) {
            if (dbData == null) {
                return null;
            }
            E result = codeToEnum.get(dbData);
            if (result == null) {
                throw new IllegalArgumentException("Unknown code: " + dbData);
            }
            return result;
        }
    }
}
