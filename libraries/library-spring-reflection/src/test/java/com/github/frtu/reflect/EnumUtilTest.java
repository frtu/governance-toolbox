package com.github.frtu.reflect;

import com.github.frtu.samples.enums.TestEnum;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class EnumUtilTest {
    @Test
    public void getEnumValues() {
        final List<TestEnum> testEnumList = EnumUtil.getEnumValues(TestEnum.class);
        assertTrue(testEnumList.contains(TestEnum.TENUM1));
        assertTrue(testEnumList.contains(TestEnum.TENUM2));
        assertTrue(testEnumList.contains(TestEnum.TENUM3));
    }

    @Test
    public void getValueObject() {
        final List<TestEnum> testEnumList = EnumUtil.getEnumValues(TestEnum.class);
        final List<Object> descriptionList = testEnumList.stream()
                .map(e -> EnumUtil.getValue(e, "description"))
                .collect(Collectors.toList());
        assertTrue(descriptionList.contains("First enum"));
        assertTrue(descriptionList.contains("Second enum"));
        assertTrue(descriptionList.contains("Third enum"));
    }

    @Test
    public void getValueTyped() {
        final List<TestEnum> testEnumList = EnumUtil.getEnumValues(TestEnum.class);
        final List<Integer> indexesList = testEnumList.stream()
                .map(e -> EnumUtil.getValue(e, "index", Integer.class))
                .collect(Collectors.toList());
        for (int i = 1; i <= 3; i++) {
            assertTrue(indexesList.contains(i));
        }
    }

    @Test
    public void getAllValues() {
        final HashMap<String, Object> allValues = EnumUtil.getAllValues(TestEnum.TENUM1);
        assertEquals(TestEnum.TENUM1.getIndex(), allValues.get("index"));
        assertEquals(TestEnum.TENUM1.getDescription(), allValues.get("description"));
    }

    @Test
    public void getSomeValues() {
        final HashMap<String, Object> someValues = EnumUtil.getSomeValues(TestEnum.TENUM1, "index");
        assertEquals(TestEnum.TENUM1.getIndex(), someValues.get("index"));
        assertNull(someValues.get("description"));
    }
}