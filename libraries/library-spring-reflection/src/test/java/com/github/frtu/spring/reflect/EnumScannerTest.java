package com.github.frtu.spring.reflect;

import com.github.frtu.samples.TestEnum;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class EnumScannerTest {
    @Test
    public void getEnumValues() {
        final TestEnum[] enumValues = EnumScanner.getEnumValues(TestEnum.class);
        final List<TestEnum> testEnumList = Arrays.asList(enumValues);
        assertTrue(testEnumList.contains(TestEnum.ENUM1));
        assertTrue(testEnumList.contains(TestEnum.ENUM2));
        assertTrue(testEnumList.contains(TestEnum.ENUM3));
    }

    @Test
    public void getValueObject() {
        final TestEnum[] enumValues = EnumScanner.getEnumValues(TestEnum.class);
        final List<TestEnum> testEnumList = Arrays.asList(enumValues);
        final List<Object> descriptionList = testEnumList.stream()
                .map(e -> EnumScanner.getValue(e, "description"))
                .collect(Collectors.toList());
        assertTrue(descriptionList.contains("First enum"));
        assertTrue(descriptionList.contains("Second enum"));
        assertTrue(descriptionList.contains("Third enum"));
    }

    @Test
    public void getValueTyped() {
        final TestEnum[] enumValues = EnumScanner.getEnumValues(TestEnum.class);
        final List<TestEnum> testEnumList = Arrays.asList(enumValues);
        final List<Integer> indexesList = testEnumList.stream()
                .map(e -> EnumScanner.getValue(e, "index", Integer.class))
                .collect(Collectors.toList());
        for (int i = 1; i <= 3; i++) {
            assertTrue(indexesList.contains(i));
        }
    }
}