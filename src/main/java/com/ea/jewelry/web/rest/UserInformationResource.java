package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.UserInformation;
import com.ea.jewelry.repository.UserInformationRepository;
import com.ea.jewelry.web.rest.util.HeaderUtil;
import com.ea.jewelry.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserInformation.
 */
@RestController
@RequestMapping("/api")
public class UserInformationResource {

    private final Logger log = LoggerFactory.getLogger(UserInformationResource.class);

    @Inject
    private UserInformationRepository userInformationRepository;

    /**
     * POST  /userInformations -> Create a new userInformation.
     */
    @RequestMapping(value = "/userInformations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInformation> createUserInformation(@RequestBody UserInformation userInformation) throws URISyntaxException {
        log.debug("REST request to save UserInformation : {}", userInformation);
        if (userInformation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userInformation cannot already have an ID").body(null);
        }
        UserInformation result = userInformationRepository.save(userInformation);
        return ResponseEntity.created(new URI("/api/userInformations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userInformation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userInformations -> Updates an existing userInformation.
     */
    @RequestMapping(value = "/userInformations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInformation> updateUserInformation(@RequestBody UserInformation userInformation) throws URISyntaxException {
        log.debug("REST request to update UserInformation : {}", userInformation);
        if (userInformation.getId() == null) {
            return createUserInformation(userInformation);
        }
        UserInformation result = userInformationRepository.save(userInformation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userInformation", userInformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userInformations -> get all the userInformations.
     */
    @RequestMapping(value = "/userInformations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserInformation>> getAllUserInformations(Pageable pageable)
        throws URISyntaxException {
        Page<UserInformation> page = userInformationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userInformations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userInformations/:id -> get the "id" userInformation.
     */
    @RequestMapping(value = "/userInformations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserInformation> getUserInformation(@PathVariable Long id) {
        log.debug("REST request to get UserInformation : {}", id);
        return Optional.ofNullable(userInformationRepository.findOne(id))
            .map(userInformation -> new ResponseEntity<>(
                userInformation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userInformations/:id -> delete the "id" userInformation.
     */
    @RequestMapping(value = "/userInformations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserInformation(@PathVariable Long id) {
        log.debug("REST request to delete UserInformation : {}", id);
        userInformationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userInformation", id.toString())).build();
    }
}
