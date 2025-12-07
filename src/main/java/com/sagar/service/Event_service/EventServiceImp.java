package com.sagar.service.Event_service;

import com.sagar.model.Event;
import com.sagar.model.Restaurant;
import com.sagar.repository.EventRepository;
import com.sagar.request.CreateEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImp implements EventService{

    @Autowired
    private EventRepository eventRepository;
    @Override
    public Event createEvent(CreateEventRequest req, Restaurant restaurant) {
        Event event = new Event();
        event.setName(req.getName());
        event.setLocation(req.getLocation());
        event.setImageUrl(req.getImageUrl());
        event.setStartedAt(req.getStartedAt());
        event.setEndsAt(req.getEndsAt());
        event.setRestaurant(restaurant);

        Event savedEvent = eventRepository.save(event);
        restaurant.getEvents().add(savedEvent);

        return savedEvent;
    }

    @Override
    public List<Event> getAllEventsOfRestaurant(Long restaurantId) {
        return eventRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public void deleteEvent(Long eventId) throws Exception {
        Event event = findEventById(eventId);
        Restaurant restaurant = event.getRestaurant();
        eventRepository.delete(event);

    }

    public Event findEventById(Long id) throws Exception{
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty())
            throw new  Exception("Event not exist...");
        return event.get();
    }
}
