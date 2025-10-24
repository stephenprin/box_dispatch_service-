package com.box.dispatch_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter
@Builder
public class ItemDto {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "[a-zA-Z0-9_-]+", message = "Name must contain only letters, numbers, '-', or '_'")
    @Valid
    private String name;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;
}