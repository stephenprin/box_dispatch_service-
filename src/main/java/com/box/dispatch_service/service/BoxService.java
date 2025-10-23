package com.box.dispatch_service.service;



import com.box.dispatch_service.dto.BoxDto;
import com.box.dispatch_service.dto.ItemDto;
import com.box.dispatch_service.entity.Box;
import com.box.dispatch_service.entity.Item;

import java.util.List;

public interface BoxService {
    Box createBox(BoxDto boxDto);
    Box loadBox(String txref, List<ItemDto> itemsDto);
    List<Item> getLoadedItems(String txref);
    List<Box> getAvailableBoxes();
    Integer getBatteryLevel(String txref);

}
