package sti.project.template.base.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersistableEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    private static final Map<Class<?>, PersistableEnumConverter<?>> CONVERTERS = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return (Converter<String, T>) CONVERTERS.computeIfAbsent(targetType,
                k -> new PersistableEnumConverter<>((Class<Enum<?>>) targetType));
    }

    private static class PersistableEnumConverter<E extends Enum<?>> implements Converter<String, E> {

        private final Class<E> enumClass;
        private final Map<Integer, E> codeToEnum;
        private final Map<String, E> nameToEnum;
        private final boolean isPersistable;

        @SuppressWarnings("unchecked")
        public PersistableEnumConverter(Class<E> enumClass) {
            this.enumClass = enumClass;
            this.isPersistable = PersistableEnum.class.isAssignableFrom(enumClass);

            E[] constants = enumClass.getEnumConstants();

            if (isPersistable) {
                this.codeToEnum = Arrays.stream(constants)
                        .collect(Collectors.toMap(
                                e -> ((PersistableEnum<Integer>) e).getCode(),
                                Function.identity()));
            } else {
                this.codeToEnum = Map.of();
            }

            this.nameToEnum = Arrays.stream(constants)
                    .collect(Collectors.toMap(
                            e -> ((Enum<?>) e).name(),
                            Function.identity()));
        }

        @Override
        public E convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }

            if (isPersistable) {
                try {
                    int code = Integer.parseInt(source);
                    E result = codeToEnum.get(code);
                    if (result != null) {
                        return result;
                    }
                    throw new IllegalArgumentException(
                            String.format("Unknown code %d for enum %s. Valid codes: %s",
                                    code, enumClass.getSimpleName(), codeToEnum.keySet()));
                } catch (NumberFormatException ignored) {
                }
            }

            E result = nameToEnum.get(source.toUpperCase());
            if (result != null) {
                return result;
            }

            throw new IllegalArgumentException(
                    String.format("Unknown value '%s' for enum %s. Valid values: %s",
                            source, enumClass.getSimpleName(), nameToEnum.keySet()));
        }
    }
}
