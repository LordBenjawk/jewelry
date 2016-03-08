package com.ea.jewelry.repository;

import com.ea.jewelry.domain.ShoppingCart;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShoppingCart entity.
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    @Query("select shoppingCart from ShoppingCart shoppingCart where shoppingCart.user.login = ?#{principal.username}")
    List<ShoppingCart> findByUserIsCurrentUser();

}
