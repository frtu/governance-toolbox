package com.github.frtu.samples;

public enum TestEnum {
    ENUM1(1, "First enum"),
    ENUM2(2, "Second enum"),
    ENUM3(3, "Third enum");

    private Integer index;
    private String description;

    TestEnum(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
