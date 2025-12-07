package com.sagar.controller.food_controller;

import com.sagar.model.Food;
import com.sagar.model.Restaurant;
import com.sagar.model.Users;
import com.sagar.request.CreateFoodRequest;
import com.sagar.response.MessageResponse;
import com.sagar.service.UserService;
import com.sagar.service.food_service.FoodService;
import com.sagar.service.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/admin/food")
@RestController
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req, @RequestHeader("Authorization") String jwt) throws Exception{

        Users user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req,req.getCategory(),restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception{

        Users user = userService.findUserByJwtToken(jwt);

        foodService.deleteFood(id);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("food deleted successfully");

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable Long  id, @RequestHeader("Authorization") String jwt) throws Exception{

        Users user = userService.findUserByJwtToken(jwt);

        Food food = foodService.updateFoodAvailabilityStatus(id);

        return new ResponseEntity<>(food, HttpStatus.CREATED);

    }

}
