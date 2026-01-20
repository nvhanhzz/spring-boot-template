package sti.project.template.base.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersistableEnumDeserializer<E extends Enum<E> & PersistableEnum<Integer>>
        extends JsonDeserializer<E> implements ContextualDeserializer {

    private Class<E> enumClass;
    private Map<Integer, E> codeToEnum;
    private Map<String, E> nameToEnum;

    public PersistableEnumDeserializer() {
    }

    public PersistableEnumDeserializer(Class<E> enumClass) {
        this.enumClass = enumClass;
        this.codeToEnum = Arrays.stream(enumClass.getEnumConstants())
                .collect(Collectors.toMap(PersistableEnum::getCode, Function.identity()));
        this.nameToEnum = Arrays.stream(enumClass.getEnumConstants())
                .collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    @Override
    public E deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();

        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            int code = Integer.parseInt(value);
            E result = codeToEnum.get(code);
            if (result != null) {
                return result;
            }
            throw InvalidFormatException.from(p,
                    String.format("Unknown code %d for enum %s", code, enumClass.getSimpleName()),
                    value, enumClass);
        } catch (NumberFormatException e) {
            E result = nameToEnum.get(value.toUpperCase());
            if (result != null) {
                return result;
            }
            throw InvalidFormatException.from(p,
                    String.format("Unknown value '%s' for enum %s", value, enumClass.getSimpleName()),
                    value, enumClass);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        Class<?> rawClass = ctxt.getContextualType().getRawClass();
        if (Enum.class.isAssignableFrom(rawClass) && PersistableEnum.class.isAssignableFrom(rawClass)) {
            return new PersistableEnumDeserializer<>((Class<E>) rawClass);
        }
        return this;
    }
}
