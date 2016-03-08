package com.ea.jewelry.repository;

import com.ea.jewelry.domain.Item;
import com.ea.jewelry.domain.ItemInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Item entity.
 */
public interface ItemRepository extends JpaRepository<Item,Long> {
    Set<Item> findAllByItemInformation(ItemInformation itemInformation);
}
