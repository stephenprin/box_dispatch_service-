package com.box.dispatch_service.service;


import com.box.dispatch_service.dto.BoxDto;
import com.box.dispatch_service.dto.ItemDto;
import com.box.dispatch_service.entity.Box;
import com.box.dispatch_service.entity.BoxState;
import com.box.dispatch_service.entity.Item;
import com.box.dispatch_service.repository.BoxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Transactional
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;
    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public Box createBox(BoxDto boxDto) {
        Box box = Box.builder()
                .txref(generateUniqueTxref())
                .weightLimit(boxDto.getWeightLimit())
                .batteryCapacity(boxDto.getBatteryCapacity() != null ? boxDto.getBatteryCapacity() : 100)
                .state(boxDto.getState() != null ? boxDto.getState() : BoxState.IDLE)
                .build();
        return boxRepository.save(box);
    }

    @Override
    public Box loadBox(String txref, List<ItemDto> itemsDto) {
        Box box = getBoxByTxref(txref);

        if (!BoxState.IDLE.equals(box.getState())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box must be IDLE to load");
        }
        if (box.getBatteryCapacity() < 25) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Battery less than 25% – cannot load");
        }

        double addedWeight = itemsDto.stream().mapToDouble(ItemDto::getWeight).sum();
        if (box.getCurrentWeight() + addedWeight > box.getWeightLimit()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weight limit exceeded");
        }

        box.setState(BoxState.LOADING);

        itemsDto.forEach(itemDto -> {
            Item item = Item.builder()
                    .name(itemDto.getName())
                    .weight(itemDto.getWeight())
                    .code(generateCode())
                    .box(box)
                    .build();
            box.addItem(item);
        });

        box.setState(BoxState.LOADED);
        return boxRepository.save(box);
    }

    @Override
    public List<Item> getLoadedItems(String txref) {
        return getBoxByTxref(txref).getItems();
    }

    @Override
    public List<Box> getAvailableBoxes() {
        return boxRepository.findByStateAndBatteryCapacityGreaterThanEqual(BoxState.IDLE, 25);
    }

    @Override
    public Integer getBatteryLevel(String txref) {
        return getBoxByTxref(txref).getBatteryCapacity();
    }


    private Box getBoxByTxref(String txref) {
        return boxRepository.findByTxref(txref)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box not found"));
    }

    private String generateUniqueTxref() {
        String txref;
        do {
            txref = "BOX-" + UUID.randomUUID().toString().substring(0, 11).toUpperCase();
        } while (boxRepository.findByTxref(txref).isPresent());
        return txref;
    }



    public String generateCode() {
        long next = counter.incrementAndGet();
        return String.format("ITEM-%04d", next);
    }

}
