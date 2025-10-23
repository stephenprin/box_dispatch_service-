package com.box.dispatch_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Size(max = 20)
    @Column(unique = true, nullable = false, length = 20)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String txref;

    @Max(500)
    private Double weightLimit;

    @Min(0)
    @Max(100)
    private Integer batteryCapacity = 100;

    @Enumerated(EnumType.STRING)
    private BoxState state = BoxState.IDLE;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "box",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @Builder.Default
    private List<Item> items = new ArrayList<>();


    public Box() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        items.add(item);
        item.setBox(this);
    }

    public double getCurrentWeight() {
        return Optional.ofNullable(items)
                .orElse(List.of())
                .stream()
                .mapToDouble(Item::getWeight)
                .sum();
    }
}
