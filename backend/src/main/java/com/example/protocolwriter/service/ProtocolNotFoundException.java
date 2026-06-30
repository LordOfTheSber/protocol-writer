package com.example.protocolwriter.service;

import java.util.UUID;

public class ProtocolNotFoundException extends RuntimeException {

    public ProtocolNotFoundException(UUID id) {
        super("Протокол не найден: " + id);
    }
}
