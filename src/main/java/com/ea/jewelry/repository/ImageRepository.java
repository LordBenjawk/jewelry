package com.ea.jewelry.repository;

import com.ea.jewelry.domain.Image;
import com.ea.jewelry.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Image entity.
 */
public interface ImageRepository extends JpaRepository<Image,Long> {
    Set<Image> findAllByItem(Item item);
}
