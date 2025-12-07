package com.sagar.repository;

import com.sagar.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByRestaurantId(Long restaurantId);
}
