package com.box.dispatch_service.dto;


import com.box.dispatch_service.entity.BoxState;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxDto {
    private Double weightLimit;
    private Integer batteryCapacity;
    private BoxState state;
}
