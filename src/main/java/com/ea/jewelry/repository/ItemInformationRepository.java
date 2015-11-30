package com.ea.jewelry.repository;

import com.ea.jewelry.domain.ItemInformation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemInformation entity.
 */
public interface ItemInformationRepository extends JpaRepository<ItemInformation,Long> {

    @Query("select itemInformation from ItemInformation itemInformation where itemInformation.user.login = ?#{principal.username}")
    List<ItemInformation> findByUserIsCurrentUser();

}
