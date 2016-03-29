package com.ea.jewelry.repository;

import com.ea.jewelry.domain.ShoppingCart;
import com.ea.jewelry.domain.ShoppingCartDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the ShoppingCartDetails entity.
 */
public interface ShoppingCartDetailsRepository extends JpaRepository<ShoppingCartDetails,Long> {
    List<ShoppingCartDetails> findByShoppingCart(ShoppingCart shoppingCart);
}
