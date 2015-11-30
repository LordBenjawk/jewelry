package com.ea.jewelry.repository;

import com.ea.jewelry.domain.Color;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Color entity.
 */
public interface ColorRepository extends JpaRepository<Color,Long> {

}
