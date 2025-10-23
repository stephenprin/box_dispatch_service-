package com.box.dispatch_service.dto;

import com.box.dispatch_service.entity.BoxState;
import com.box.dispatch_service.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxResponse {

    private String txref;

    private Double weightLimit;

    private Integer batteryCapacity;

    private BoxState state;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private List<Item> items;
    private  Double currentWeight;

}
