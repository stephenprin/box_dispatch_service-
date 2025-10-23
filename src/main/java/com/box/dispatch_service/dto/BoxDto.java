package com.box.dispatch_service.dto;


import com.box.dispatch_service.entity.BoxState;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
public class BoxDto {
    @NotNull(message = "Weight limit is required")
    @Max(value = 500, message = "Weight limit must be at most 500 grams")
    @Positive(message = "Weight limit must be positive")
    private Double weightLimit;

    @Min(value = 0, message = "Battery capacity must be at least 0")
    @Max(value = 100, message = "Battery capacity must be at most 100")
    private Integer batteryCapacity;

    private BoxState state;
}
