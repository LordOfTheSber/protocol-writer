package com.example.protocolwriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ProtocolWriterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProtocolWriterApplication.class, args);
    }
}
