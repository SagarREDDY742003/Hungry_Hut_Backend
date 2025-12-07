package com.sagar.service.restaurant_services;

import com.sagar.dto.RestaurantDto;
import com.sagar.model.Restaurant;
import com.sagar.model.Users;
import com.sagar.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest req, Users user);

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;

    public void deleteRestaurant(Long restaurantId) throws Exception;

    public List<Restaurant> getAllRestaurants();

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant findRestaurantById(Long id) throws Exception;

    public Restaurant getRestaurantByUserId(Long userId) throws Exception;

    public RestaurantDto addToFavorites(Long restaurantId, Users user) throws Exception;

    public Restaurant updateRestaurantStatus(Long id) throws Exception;

}
