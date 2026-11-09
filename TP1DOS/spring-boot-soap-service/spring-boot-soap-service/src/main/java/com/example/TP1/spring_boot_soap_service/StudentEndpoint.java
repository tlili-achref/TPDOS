package com.example.TP1.spring_boot_soap_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.TP1.springbootsoapservice.model.StudentDetailsRequest;
import com.example.TP1.springbootsoapservice.model.StudentDetailsResponse;

@Endpoint
public class StudentEndpoint {

    private static final String NAMESPACE_URI = "http://www.exemple.org/student";

    private final StudentRepository studentRepository;

    @Autowired
    public StudentEndpoint(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "StudentDetailsRequest")
    @ResponsePayload
    public StudentDetailsResponse getStudent(@RequestPayload StudentDetailsRequest request) {
        StudentDetailsResponse response = new StudentDetailsResponse();
        response.setStudent(studentRepository.findStudent(request.getName()));
        return response;
    }
}