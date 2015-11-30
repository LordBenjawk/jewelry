package com.ea.jewelry.repository;

import com.ea.jewelry.domain.Sizes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sizes entity.
 */
public interface SizesRepository extends JpaRepository<Sizes,Long> {

}
