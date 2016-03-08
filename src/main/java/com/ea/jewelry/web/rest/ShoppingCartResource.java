package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.ShoppingCart;
import com.ea.jewelry.repository.ShoppingCartRepository;
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
 * REST controller for managing ShoppingCart.
 */
@RestController
@RequestMapping("/api")
public class ShoppingCartResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingCartResource.class);

    @Inject
    private ShoppingCartRepository shoppingCartRepository;

    /**
     * POST  /shoppingCarts -> Create a new shoppingCart.
     */
    @RequestMapping(value = "/shoppingCarts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
        log.debug("REST request to save ShoppingCart : {}", shoppingCart);
        if (shoppingCart.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new shoppingCart cannot already have an ID").body(null);
        }
        ShoppingCart result = shoppingCartRepository.save(shoppingCart);
        return ResponseEntity.created(new URI("/api/shoppingCarts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shoppingCart", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shoppingCarts -> Updates an existing shoppingCart.
     */
    @RequestMapping(value = "/shoppingCarts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShoppingCart> updateShoppingCart(@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
        log.debug("REST request to update ShoppingCart : {}", shoppingCart);
        if (shoppingCart.getId() == null) {
            return createShoppingCart(shoppingCart);
        }
        ShoppingCart result = shoppingCartRepository.save(shoppingCart);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shoppingCart", shoppingCart.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shoppingCarts -> get all the shoppingCarts.
     */
    @RequestMapping(value = "/shoppingCarts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ShoppingCart>> getAllShoppingCarts(Pageable pageable)
        throws URISyntaxException {
        Page<ShoppingCart> page = shoppingCartRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shoppingCarts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shoppingCarts/:id -> get the "id" shoppingCart.
     */
    @RequestMapping(value = "/shoppingCarts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long id) {
        log.debug("REST request to get ShoppingCart : {}", id);
        return Optional.ofNullable(shoppingCartRepository.findOne(id))
            .map(shoppingCart -> new ResponseEntity<>(
                shoppingCart,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shoppingCarts/:id -> delete the "id" shoppingCart.
     */
    @RequestMapping(value = "/shoppingCarts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingCart : {}", id);
        shoppingCartRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shoppingCart", id.toString())).build();
    }
}
