package com.ea.jewelry.repository;

import com.ea.jewelry.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Status entity.
 */
public interface StatusRepository extends JpaRepository<Status,Long> {
    Status findOneByName(String name);
}
