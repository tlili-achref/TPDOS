package com.example.TP1.spring_boot_soap_client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.TP1.springbootsoapclient.model.StudentDetailsRequest;
import com.example.TP1.springbootsoapclient.model.StudentDetailsResponse;

public class StudentClient extends WebServiceGatewaySupport {

    public StudentDetailsResponse getStudent(String name) {
        StudentDetailsRequest request = new StudentDetailsRequest();
        request.setName(name);
        return (StudentDetailsResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}