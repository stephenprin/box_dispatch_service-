package com.box.dispatch_service.service;



import com.box.dispatch_service.dto.BoxDto;
import com.box.dispatch_service.dto.BoxResponse;
import com.box.dispatch_service.dto.ItemDto;
import com.box.dispatch_service.dto.ItemResponse;
import com.box.dispatch_service.entity.Box;
import com.box.dispatch_service.entity.Item;

import java.util.List;

public interface BoxService {
    BoxResponse createBox(BoxDto boxDto);
    BoxResponse loadBox(String txref, List<ItemDto> itemsDto);
    List<ItemResponse> getLoadedItems(String txref);
    List<BoxResponse> getAvailableBoxes();
    Integer getBatteryLevel(String txref);

}
