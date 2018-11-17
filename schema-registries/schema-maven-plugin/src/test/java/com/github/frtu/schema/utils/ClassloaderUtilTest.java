package com.github.frtu.schema.utils;

import org.junit.Assert;
import org.junit.Test;
import tests.pojo.UserImpl;
import tests.pojo.UserInterface;

import java.util.Set;

import static org.junit.Assert.*;

public class ClassloaderUtilTest {
    @Test
    public void scanPackageForSubtypesOf() {
        final Set<Class<? extends UserInterface>> tests = ClassloaderUtil.scanPackageForSubtypesOf("tests", UserInterface.class);
        Assert.assertEquals(1, tests.size());
        Assert.assertEquals(UserImpl.class, tests.toArray()[0]);
    }

    @Test
    public void scanPackageForSubtypesOfString() {
        final Set<Class<?>> tests = ClassloaderUtil.scanPackageForSubtypesOf(ClassloaderUtilTest.class.getClassLoader(), "tests", "tests.pojo.UserInterface");
        Assert.assertEquals(1, tests.size());
        Assert.assertEquals(UserImpl.class, tests.toArray()[0]);
    }
}