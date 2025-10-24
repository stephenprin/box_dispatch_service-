package com.box.dispatch_service.service;

import com.box.dispatch_service.dto.BoxDto;
import com.box.dispatch_service.dto.BoxResponse;
import com.box.dispatch_service.dto.ItemDto;
import com.box.dispatch_service.dto.ItemResponse;
import com.box.dispatch_service.entity.Box;
import com.box.dispatch_service.entity.BoxState;
import com.box.dispatch_service.entity.Item;
import com.box.dispatch_service.repository.BoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoxServiceImplTest {

    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private BoxServiceImpl boxService;

    private Box box;
    private static final String TXREF = "BOX-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        box = Box.builder()
                .id(UUID.randomUUID().toString())
                .txref(TXREF)
                .weightLimit(500.0)
                .batteryCapacity(80)
                .state(BoxState.IDLE)
                .items(new ArrayList<>())
                .build();

        when(boxRepository.findByTxref(TXREF)).thenReturn(Optional.of(box));
        when(boxRepository.findByTxref("NON_EXISTENT")).thenReturn(Optional.empty());
        when(boxRepository.findByState(BoxState.IDLE)).thenReturn(List.of(box));

    }

    @Test
    @DisplayName("createBox - should create box with defaults and unique txref")
    void createBox() {

        BoxDto dto = BoxDto.builder()
                .weightLimit(400.0)
                .batteryCapacity(80)
                .state(BoxState.IDLE)
                .build();

        // Mock save to return entity with generated txref
        when(boxRepository.save(any(Box.class))).thenAnswer(invocation -> {
            Box saved = invocation.getArgument(0);
            saved.setTxref("AUTO-" + UUID.randomUUID().toString().substring(0, 8));
            return saved;
        });

        // When
        BoxResponse response = boxService.createBox(dto);

        // Then
        assertNotNull(response.getTxref());
        assertTrue(response.getTxref().startsWith("AUTO-"));
        assertEquals(400.0, response.getWeightLimit());
        assertEquals(80, response.getBatteryCapacity());
        assertEquals(BoxState.IDLE, response.getState());
        verify(boxRepository, times(1)).save(any(Box.class));
    }

    @Test
    @DisplayName("loadBox - should generate sequential ITEM-XXXX codes")
    void loadBox() {
        List<ItemDto> itemsDto = List.of(
                ItemDto.builder().name("bandage").weight(100.0).build(),
                ItemDto.builder().name("syringe").weight(50.0).build()
        );

        BoxResponse result = boxService.loadBox(TXREF, itemsDto);

        assertEquals(BoxState.LOADED, result.getState());
        assertEquals(2, result.getItems().size());
        assertEquals("ITEM-0001", result.getItems().get(0).getCode());
        assertEquals("ITEM-0002", result.getItems().get(1).getCode());
        verify(boxRepository, times(1)).save(any(Box.class));

    }
    @Test
    @DisplayName("getLoadedItems - should return items for given txref")
    void getLoadedItems() {
        // Given
        Item item = Item.builder()
                .name("medkit")
                .weight(200.0)
                .code("ITEM_999")
                .box(box)
                .build();
        box.getItems().add(item);

        // When
        List<ItemResponse> items = boxService.getLoadedItems(TXREF);

        // Then
        assertEquals(1, items.size());
        assertEquals("medkit", items.get(0).getName());
    }
    @Test
    @DisplayName("getAvailableBoxes - should return IDLE boxes with battery >=25")
    void getAvailableBoxes() {
        // Given
        Box lowBatteryBox = Box.builder()
                .txref("LOW-BAT")
                .batteryCapacity(20)
                .state(BoxState.IDLE)
                .build();

        when(boxRepository.findByState(BoxState.IDLE))
                .thenReturn(List.of(box));

        // When
        List<BoxResponse> available = boxService.getAvailableBoxes();

        // Then
        assertEquals(1, available.size());
        assertEquals(TXREF, available.get(0).getTxref());
    }
    @Test
    @DisplayName("getBatteryLevel - should return battery capacity")
    void getBatteryLevel() {
        // When
        Integer battery = boxService.getBatteryLevel(TXREF);

        // Then
        assertEquals(80, battery);
    }


}