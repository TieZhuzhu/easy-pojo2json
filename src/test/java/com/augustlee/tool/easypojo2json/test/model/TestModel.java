package com.augustlee.tool.easypojo2json.test.model;

import com.augustlee.tool.easypojo2json.test.MyTestCase;

import java.util.Arrays;

import static junit.framework.Assert.assertTrue;

public class TestModel {

    protected final MyTestCase testCase;

    public TestModel(MyTestCase testCase) {
        this.testCase = testCase;
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        assertTrue(Arrays.equals(expected, actual));
    }
}
