package com.box.dispatch_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Pattern(regexp = "[a-zA-Z0-9_-]+")
    private String name;

    private Double weight;

    @Pattern(regexp = "[A-Z0-9_-]+")
    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "box_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Box box;
}
