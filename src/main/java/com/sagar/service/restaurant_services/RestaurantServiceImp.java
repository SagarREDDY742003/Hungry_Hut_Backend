package com.sagar.service.restaurant_services;

import com.sagar.dto.RestaurantDto;
import com.sagar.model.Address;
import com.sagar.model.Restaurant;
import com.sagar.model.Users;
import com.sagar.repository.AddressRepository;
import com.sagar.repository.RestaurantRepository;
import com.sagar.repository.UserRepository;
import com.sagar.request.CreateRestaurantRequest;
import com.sagar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImp implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, Users user) {

        Address address = addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(req.getAddress());
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setDescription(req.getDescription());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);

        if(restaurant.getCuisineType()!=null){
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        }
        if(restaurant.getDescription()!=null){
            restaurant.setDescription(updatedRestaurant.getDescription());
        }
        if(restaurant.getName()!=null){
            restaurant.setName(updatedRestaurant.getName());
        }
        if(restaurant.getContactInformation()!=null){
            restaurant.setContactInformation(updatedRestaurant.getContactInformation());
        }
        if(restaurant.getImages()!=null){
            restaurant.setImages(updatedRestaurant.getImages());
        }
        if(restaurant.getAddress()!=null){
            restaurant.setAddress(updatedRestaurant.getAddress());
        }
        if(restaurant.getOpeningHours()!=null){
            restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {

        // Optional class is a solution to handle nullable values more effectively and avoid the infamous NullPointerException (NPE)
        Optional<Restaurant> opt = restaurantRepository.findById(id);

        if(opt.isEmpty())
            throw new Exception("restaurant not found with id "+id);
        return opt.get();
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);

    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        if(restaurant==null)
            throw new Exception("restaurant not found with ownerId "+userId);

        return restaurant;

    }

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, Users user) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantDto dto = new RestaurantDto();
        dto.setDescription(restaurant.getDescription());
        dto.setImages(restaurant.getImages());
        dto.setName(restaurant.getName());
        dto.setId(restaurant.getId());
        dto.setOpen(restaurant.isOpen());

        boolean isFavorite = false;
        List<RestaurantDto> favorites = user.getFavorites();
        for (RestaurantDto favorite: favorites){
            if(favorite.getId().equals(restaurantId)){
                isFavorite=true;
                break;
            }
        }

        if (isFavorite)
            favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        else
            favorites.add(dto);

        userRepository.save(user);

        return dto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {

        Restaurant restaurant = findRestaurantById(id);

        restaurant.setOpen(!restaurant.isOpen());

        return restaurantRepository.save(restaurant);
    }

}
