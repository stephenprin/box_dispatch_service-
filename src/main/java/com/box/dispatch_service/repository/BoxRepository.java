package com.box.dispatch_service.repository;


import com.box.dispatch_service.entity.Box;
import com.box.dispatch_service.entity.BoxState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, String> {
    Optional<Box> findByTxref(String txref);
    List<Box> findByStateAndBatteryCapacityGreaterThanEqual(BoxState state, int battery);
}
