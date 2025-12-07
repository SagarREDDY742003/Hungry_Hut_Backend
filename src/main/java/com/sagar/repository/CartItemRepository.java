package com.sagar.repository;

import com.sagar.model.Cart;
import com.sagar.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {


}
