package com.globits.da.dto;

import com.globits.da.domain.entity.Employee;

public class EmployeeDto  {
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private Integer age;

    public EmployeeDto() {
       super();
    }

    public EmployeeDto(Employee employee){
        if (employee!=null){
            this.id=employee.getId();
            this.code=employee.getCode();
            this.name=employee.getName();
            this.email=employee.getEmail();
            this.phone=employee.getPhone();
            this.age=employee.getAge();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
