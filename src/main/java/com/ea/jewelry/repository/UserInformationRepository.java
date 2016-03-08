package com.ea.jewelry.repository;

import com.ea.jewelry.domain.UserInformation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserInformation entity.
 */
public interface UserInformationRepository extends JpaRepository<UserInformation,Long> {

}
