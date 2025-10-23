package com.box.dispatch_service.controller;


import com.box.dispatch_service.dto.BoxDto;
import com.box.dispatch_service.dto.ItemDto;
import com.box.dispatch_service.entity.Box;
import com.box.dispatch_service.entity.Item;
import com.box.dispatch_service.service.BoxServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/box")
@RequiredArgsConstructor
public class BoxController {
    private final BoxServiceImpl boxService;

    @PostMapping
    public ResponseEntity<Box> create(@Valid @RequestBody BoxDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boxService.createBox(dto));
    }

    @PostMapping("/{txref}/load")
    public ResponseEntity<Box> load(@PathVariable String txref,
                                    @Valid @RequestBody List<ItemDto> items) {
        return ResponseEntity.ok(boxService.loadBox(txref, items));
    }

    @GetMapping("/{txref}/items")
    public ResponseEntity<List<Item>> items(@PathVariable String txref) {
        return ResponseEntity.ok(boxService.getLoadedItems(txref));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Box>> available() {
        return ResponseEntity.ok(boxService.getAvailableBoxes());
    }

    @GetMapping("/{txref}/battery")
    public ResponseEntity<Map<String, Integer>> battery(@PathVariable String txref) {
        return ResponseEntity.ok(Map.of("batteryCapacity", boxService.getBatteryLevel(txref)));
    }
}
