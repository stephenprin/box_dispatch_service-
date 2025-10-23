package com.box.dispatch_service.dto;

import com.box.dispatch_service.entity.Box;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

    private String name;

    private Double weight;

    private String code;

}
