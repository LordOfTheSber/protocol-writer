package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.ai.ImproveMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImproveTextRequest(
        @NotBlank String text,
        @NotNull ImproveMode mode) {
}
