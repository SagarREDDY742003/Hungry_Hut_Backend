package com.sagar.service.Event_service;

import com.sagar.model.Event;
import com.sagar.model.Restaurant;
import com.sagar.request.CreateEventRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EventService {

    public Event createEvent(CreateEventRequest req, Restaurant restaurant);

    public List<Event> getAllEventsOfRestaurant( Long restaurantId);

    public List<Event> getAllEvents();

    public void deleteEvent(Long eventId) throws Exception;

}
