package com.github.frtu.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class EnumUtil {
    public static final String ALL_ENUM_FIELD_NAME = "$VALUES";

    public static <E extends Enum> List<E> getEnumValues(Class<E> enumClass) {
        Object o = getValue(null, enumClass, ALL_ENUM_FIELD_NAME, enumClass);
        return Arrays.asList((E[]) o);
    }

    public static HashMap<String, Object> getSomeValues(final Enum anEnum, String... fieldNames) {
        return getAllValues(anEnum, new FieldPredicate.NameInArray(fieldNames));
    }

    public static HashMap<String, Object> getAllValues(final Enum anEnum) {
        return getAllValues(anEnum, FieldPredicate.isEnumInnerField());
    }

    protected static HashMap<String, Object> getAllValues(Enum anEnum, Predicate<Field> predicate) {
        final HashMap<String, Object> result = new HashMap<>();
        final Field[] fields = anEnum.getClass().getDeclaredFields();
        Arrays.stream(fields).filter(predicate).forEach(field -> {
            LOGGER.trace("Scanning fieldName:[{}] class:[{}]", field.getName(), field.getType());
            final Object value = getValue(anEnum, field, Object.class);
            result.put(field.getName(), value);
        });
        return result;
    }

    public static Object getValue(final Enum anEnum, final String fieldName) {
        return getValue(anEnum, fieldName, Object.class);
    }

    public static <T> T getValue(final Enum anEnum, final String fieldName, Class<T> returnType) {
        return getValue(anEnum, anEnum.getClass(), fieldName, returnType);
    }

    public static <T> T getValue(final Enum anEnum, final Class<? extends Enum> clazz, final String fieldName, Class<T> returnType) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Cannot find field:" + fieldName);
        }
        return getValue(anEnum, field, returnType);
    }

    public static <T> T getValue(Enum anEnum, Field field, Class<T> returnType) {
        T value = null;
        try {
            field.setAccessible(true);
            value = (T) field.get(anEnum);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot access field:" + field.getName());
        }
        return value;
    }
}
