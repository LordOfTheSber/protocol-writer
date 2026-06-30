package com.example.protocolwriter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProtocolRepository extends JpaRepository<ProtocolEntity, UUID> {
}
