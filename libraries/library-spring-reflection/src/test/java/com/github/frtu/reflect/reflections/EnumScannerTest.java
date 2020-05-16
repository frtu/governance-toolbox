package com.github.frtu.reflect.reflections;

import com.github.frtu.reflect.EnumUtil;
import com.github.frtu.samples.enums.EnumsAndNotEnums;
import com.github.frtu.samples.enums.IEnum;
import com.github.frtu.samples.enums.TestEnum;
import com.github.frtu.samples.enums.TestNoInterfaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.*;

@Slf4j
public class EnumScannerTest {
    @Test
    public void scanAllEnums() {
        final Set<Class<? extends Enum>> scanEnum = EnumScanner.of(TestEnum.class.getPackage()).scan();
        assertTrue(scanEnum.contains(TestEnum.class));
        assertTrue(scanEnum.contains(TestNoInterfaceEnum.class));
    }

    @Test
    public void scanAllEnumsForValues() {
        final Set<Class<? extends Enum>> scanEnum = EnumScanner.of(TestEnum.class.getPackage()).scan();
        final EnumUtil enumUtil = EnumUtil.of("description");

        scanEnum.stream().map(EnumUtil::getEnumValues).forEach(listOfEnums -> {
                    // A list of all the enum of one kind
                    LOGGER.debug("========== {} ============", listOfEnums.get(0).getClass());
                    listOfEnums.stream().forEach(anEnum -> {
                        final HashMap<String, Object> allValues = enumUtil.getSomeValues(anEnum);
                        LOGGER.debug("** {}", allValues);
                        assertNull(allValues.get("index"));
                        assertNotNull(allValues.get("description"));
                    });
                }
        );
    }

    @Test
    public void scanEnumInterface() {
        final Set<Class<? extends IEnum>> scanEnum = EnumScanner.of(TestEnum.class.getPackage()).scan(IEnum.class);
        assertTrue(scanEnum.contains(TestEnum.class));
        assertFalse(scanEnum.contains(TestNoInterfaceEnum.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void scanEnumsAndNotEnums() {
        EnumScanner.of(TestEnum.class.getPackage()).scan(EnumsAndNotEnums.class);
    }

    @Test
    public void scanEnumInterfaceForValues() {
        final Set<Class<? extends Enum>> scanEnum = EnumScanner.of(TestEnum.class.getPackage()).scan(IEnum.class);
        // ATTENTION : All classes implementing IEnum.class MUST be of type {@link Enum},
        //              else stream will throw exception
        scanEnum.stream().map(EnumUtil::getEnumValues).forEach(listOfEnums ->
                // Check that all the enums of this list is assignable to IEnum
                assertTrue(listOfEnums.stream().allMatch(anEnum -> IEnum.class.isAssignableFrom(anEnum.getClass())))
        );
    }
}