package com.github.frtu.samples.enums;

public enum TestEnum implements IEnum {
    ENUM1(1, "First enum"),
    ENUM2(2, "Second enum"),
    ENUM3(3, "Third enum");

    private Integer index;
    private String description;

    TestEnum(Integer index, String description) {
        this.index = index;
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
