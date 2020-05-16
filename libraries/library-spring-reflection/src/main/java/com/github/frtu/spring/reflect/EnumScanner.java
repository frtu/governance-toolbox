package com.github.frtu.spring.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class EnumScanner<E extends Enum> {
    public static <E extends Enum> E[] getEnumValues(Class<E> enumClass) {
        Object o = getValue(enumClass, null, "$VALUES", enumClass);
        return (E[]) o;
    }

    public static Object getValue(final Enum anEnum, final String fieldName) {
        return getValue(anEnum, fieldName, Object.class);
    }

    public static <T> T getValue(final Enum anEnum, final String fieldName, Class<T> type) {
        return getValue(anEnum.getClass(), anEnum, fieldName, type);
    }

    public static <T> T getValue(final Class<? extends Enum> clazz, final Enum anEnum, final String fieldName, Class<T> type) {
        T value = null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            value = (T) field.get(anEnum);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot access field:" + fieldName);
        }
        return value;
    }
}
