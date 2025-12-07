package com.sagar.controller.event_controller;

import com.sagar.model.Event;
import com.sagar.model.Restaurant;
import com.sagar.request.CreateEventRequest;
import com.sagar.response.MessageResponse;
import com.sagar.service.Event_service.EventService;
import com.sagar.service.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Event> createFood(@RequestBody CreateEventRequest req, @RequestHeader("Authorization") String jwt, @PathVariable Long restaurantId) throws Exception{

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        Event event = eventService.createEvent(req,restaurant);

        return new ResponseEntity<>(event, HttpStatus.CREATED);

    }

    @GetMapping("/restaurant/{restaurantId}")
    public  ResponseEntity<List<Event>> getAllEventsOfRestaurant(@RequestHeader("Authorization") String jwt,@PathVariable Long restaurantId) throws Exception{
        List<Event> events = eventService.getAllEventsOfRestaurant(restaurantId);
        return new ResponseEntity<>(events,HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<MessageResponse> deleteEvents(@RequestHeader("Authorization") String jwt, @PathVariable Long eventId) throws Exception{
        eventService.deleteEvent(eventId);
        MessageResponse messageResponse= new MessageResponse();
        messageResponse.setMessage("Event Deleted Successfully...");
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

}
