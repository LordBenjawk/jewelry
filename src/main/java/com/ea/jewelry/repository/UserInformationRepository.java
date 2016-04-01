package com.ea.jewelry.repository;

import com.ea.jewelry.domain.User;
import com.ea.jewelry.domain.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the UserInformation entity.
 */
public interface UserInformationRepository extends JpaRepository<UserInformation,Long> {
    @Query("select userInformation from UserInformation userInformation where userInformation.user.login = ?#{principal.username}")
    UserInformation findByUserIsCurrentUser();

    UserInformation findOneByUser(User user);
}
