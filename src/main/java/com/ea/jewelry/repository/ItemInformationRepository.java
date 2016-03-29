package com.ea.jewelry.repository;

import com.ea.jewelry.domain.ItemInformation;
import com.ea.jewelry.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemInformation entity.
 */
public interface ItemInformationRepository extends JpaRepository<ItemInformation,Long> {

    @Query("select itemInformation from ItemInformation itemInformation where itemInformation.user.login = ?#{principal.username}")
    List<ItemInformation> findByUserIsCurrentUser();

    Page<ItemInformation> findAllByStatus(Status status, Pageable pageable);
}
