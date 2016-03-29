package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.ShoppingCartDetails;
import com.ea.jewelry.repository.ShoppingCartDetailsRepository;
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
 * REST controller for managing ShoppingCartDetails.
 */
@RestController
@RequestMapping("/api")
public class ShoppingCartDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingCartDetailsResource.class);

    @Inject
    private ShoppingCartDetailsRepository shoppingCartDetailsRepository;

    /**
     * POST  /shoppingCartDetailss -> Create a new shoppingCartDetails.
     */
    @RequestMapping(value = "/shoppingCartDetailss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShoppingCartDetails> createShoppingCartDetails(@RequestBody ShoppingCartDetails shoppingCartDetails) throws URISyntaxException {
        log.debug("REST request to save ShoppingCartDetails : {}", shoppingCartDetails);
        if (shoppingCartDetails.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new shoppingCartDetails cannot already have an ID").body(null);
        }
        ShoppingCartDetails result = shoppingCartDetailsRepository.save(shoppingCartDetails);
        return ResponseEntity.created(new URI("/api/shoppingCartDetailss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shoppingCartDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shoppingCartDetailss -> Updates an existing shoppingCartDetails.
     */
    @RequestMapping(value = "/shoppingCartDetailss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShoppingCartDetails> updateShoppingCartDetails(@RequestBody ShoppingCartDetails shoppingCartDetails) throws URISyntaxException {
        log.debug("REST request to update ShoppingCartDetails : {}", shoppingCartDetails);
        if (shoppingCartDetails.getId() == null) {
            return createShoppingCartDetails(shoppingCartDetails);
        }
        ShoppingCartDetails result = shoppingCartDetailsRepository.save(shoppingCartDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shoppingCartDetails", shoppingCartDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shoppingCartDetailss -> get all the shoppingCartDetailss.
     */
    @RequestMapping(value = "/shoppingCartDetailss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ShoppingCartDetails>> getAllShoppingCartDetailss(Pageable pageable)
        throws URISyntaxException {
        Page<ShoppingCartDetails> page = shoppingCartDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shoppingCartDetailss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shoppingCartDetailss/:id -> get the "id" shoppingCartDetails.
     */
    @RequestMapping(value = "/shoppingCartDetailss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShoppingCartDetails> getShoppingCartDetails(@PathVariable Long id) {
        log.debug("REST request to get ShoppingCartDetails : {}", id);
        return Optional.ofNullable(shoppingCartDetailsRepository.findOne(id))
            .map(shoppingCartDetails -> new ResponseEntity<>(
                shoppingCartDetails,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shoppingCartDetailss/:id -> delete the "id" shoppingCartDetails.
     */
    @RequestMapping(value = "/shoppingCartDetailss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShoppingCartDetails(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingCartDetails : {}", id);
        shoppingCartDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shoppingCartDetails", id.toString())).build();
    }

}
