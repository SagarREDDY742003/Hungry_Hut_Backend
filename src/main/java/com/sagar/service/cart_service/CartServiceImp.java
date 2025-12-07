package com.sagar.service.cart_service;

import com.sagar.model.Cart;
import com.sagar.model.CartItem;
import com.sagar.model.Food;
import com.sagar.model.Users;
import com.sagar.repository.CartItemRepository;
import com.sagar.repository.CartRepository;
import com.sagar.repository.FoodRepository;
import com.sagar.repository.UserRepository;
import com.sagar.request.AddCartItemRequest;
import com.sagar.service.UserService;
import com.sagar.service.food_service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImp implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {

        Users user = userService.findUserByJwtToken(jwt);
        Food food = foodService.findFoodById(req.getFoodId());
        Cart cart = cartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem: cart.getItems()){
            if (cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity()+req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(),newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity()*food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        cart.getItems().add(savedCartItem);

        return savedCartItem;

    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if(cartItemOptional.isEmpty()){
            throw new Exception("cart item is not found");
        }

        CartItem item = cartItemOptional.get();
        item.setQuantity(quantity);
        item.setTotalPrice(item.getFood().getPrice()*quantity);

        return cartItemRepository.save(item);

    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {

        Users user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if(cartItemOptional.isEmpty()){
            throw new Exception("cart item is not found");
        }

        CartItem item = cartItemOptional.get();
        cart.getItems().remove(item);

        return cartRepository.save(cart);

    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {

        long total = 0L;

        for (CartItem cartItem: cart.getItems()){
            total+=cartItem.getFood().getPrice()*cartItem.getQuantity();
        }

        return total;

    }

    @Override
    public Cart findCartById(Long id) throws Exception {

        Optional<Cart> optionalCart = cartRepository.findById(id);

        if (optionalCart.isEmpty())
            throw new Exception("Cart not found");

        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
//        User user = userService.findUserByJwtToken(jwt);
        Cart cart =  cartRepository.findByCustomerId(userId);
        cart.setTotal(calculateCartTotals(cart));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {

        Cart cart = findCartByUserId(userId);
        cart.getItems().clear();

        return cartRepository.save(cart);

    }
}
