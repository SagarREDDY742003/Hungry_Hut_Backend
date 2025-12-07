package com.sagar.controller.food_controller;

import com.sagar.model.Food;
import com.sagar.model.Restaurant;
import com.sagar.model.Users;
import com.sagar.request.CreateFoodRequest;
import com.sagar.service.UserService;
import com.sagar.service.food_service.FoodService;
import com.sagar.service.restaurant_services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class CustomerFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name, @RequestHeader("Authorization") String jwt) throws Exception{

        Users user = userService.findUserByJwtToken(jwt);

        List<Food> foods = foodService.searchFood(name);

        return new ResponseEntity<>(foods, HttpStatus.OK);

    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Food>> getRestaurantFood(
            @RequestParam(required = false) boolean vegetarian,
            @RequestParam(required = false) boolean nonveg,
            @RequestParam(required = false) boolean seasonal,
            @RequestParam(required = false) String food_category,
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception{

        Users user = userService.findUserByJwtToken(jwt);

        List<Food> foods = foodService.getRestaurantsFood(id,vegetarian,nonveg,seasonal,food_category);

        return new ResponseEntity<>(foods, HttpStatus.OK);

    }
}
