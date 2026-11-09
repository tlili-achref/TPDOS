package com.example.TP1.spring_boot_soap_service;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.example.TP1.springbootsoapservice.model.Student;

@Component
public class StudentRepository {

    private static final Map<String, Student> students = new HashMap<>();

    @PostConstruct
    public void initData() {
        students.put("Sajal", create("Sajal", 5, "Pune"));
        students.put("Kajal", create("Kajal", 5, "Chicago"));
        students.put("Lokesh", create("Lokesh", 6, "Delhi"));
        students.put("Sukesh", create("Sukesh", 7, "Noida"));
    }

    private Student create(String name, int standard, String address) {
        Student s = new Student();
        s.setName(name);
        s.setStandard(standard);
        s.setAddress(address);
        return s;
    }

    public Student findStudent(String name) {
        Assert.notNull(name, "The Student's name must not be null");
        return students.get(name);
    }
}