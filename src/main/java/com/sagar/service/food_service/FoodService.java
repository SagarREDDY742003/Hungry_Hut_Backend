package com.sagar.service.food_service;

import com.sagar.model.Category;
import com.sagar.model.Food;
import com.sagar.model.Restaurant;
import com.sagar.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant);

    public void deleteFood(Long foodId) throws Exception;

    public List<Food> getRestaurantsFood(Long restaurantId,boolean isVegetarian, boolean isNonVeg, boolean isSeasonal, String foodCategory);

    public List<Food> searchFood(String keyword);

    public Food findFoodById(Long foodId) throws Exception;

    public Food updateFoodAvailabilityStatus(Long foodId) throws Exception;

}
