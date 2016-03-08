package com.ea.jewelry.repository;

import com.ea.jewelry.domain.ShoppingCartDetails;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ShoppingCartDetails entity.
 */
public interface ShoppingCartDetailsRepository extends JpaRepository<ShoppingCartDetails,Long> {

}
