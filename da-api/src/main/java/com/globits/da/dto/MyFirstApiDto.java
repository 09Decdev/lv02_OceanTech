package com.globits.da.dto;

public class MyFirstApiDto {
    private Integer code;
    private String name;
    private Integer age;

    public MyFirstApiDto() {
    }

    public MyFirstApiDto(Integer code, String name, Integer age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
