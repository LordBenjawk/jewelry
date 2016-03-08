package com.ea.jewelry.repository;

import com.ea.jewelry.domain.Size;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Size entity.
 */
public interface SizeRepository extends JpaRepository<Size,Long> {

}
