package com.box.dispatch_service.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private String name;
    private Double weight;
}