package com.example.TP1.spring_boot_soap_client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.TP1.springbootsoapclient.model.StudentDetailsResponse;

@SpringBootApplication
public class SpringBootSoapClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSoapClientApplication.class, args);
    }

    @Bean
    CommandLineRunner run(StudentClient client) {
        return args -> {
            String name = args.length > 0 ? args[0] : "Sajal";
            StudentDetailsResponse response = client.getStudent(name);
            if (response.getStudent() == null) {
                System.out.println("Aucun étudiant trouvé : " + name);
            } else {
                System.out.println("Nom     : " + response.getStudent().getName());
                System.out.println("Standard: " + response.getStudent().getStandard());
                System.out.println("Adresse : " + response.getStudent().getAddress());
            }
        };
    }
}