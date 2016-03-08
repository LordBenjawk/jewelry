package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.Item;
import com.ea.jewelry.domain.ItemInformation;
import com.ea.jewelry.repository.ItemInformationRepository;
import com.ea.jewelry.repository.ItemRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ItemInformation.
 */
@RestController
@RequestMapping("/api")
public class ItemInformationResource {

    private final Logger log = LoggerFactory.getLogger(ItemInformationResource.class);

    @Inject
    private ItemInformationRepository itemInformationRepository;

    @Inject
    private ItemRepository itemRepository;

    /**
     * POST  /itemInformations -> Create a new itemInformation.
     */
    @RequestMapping(value = "/itemInformations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemInformation> createItemInformation(@Valid @RequestBody ItemInformation itemInformation) throws URISyntaxException {
        log.debug("REST request to save ItemInformation : {}", itemInformation);
        if (itemInformation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new itemInformation cannot already have an ID").body(null);
        }
        if (itemInformation.getVip() == null) itemInformation.setVip(false);
        ItemInformation result = itemInformationRepository.save(itemInformation);
        Item newItem = new Item();
        newItem.setName("");
        newItem.setItemInformation(result);
        itemRepository.save(newItem);
        return ResponseEntity.created(new URI("/api/itemInformations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemInformation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itemInformations -> Updates an existing itemInformation.
     */
    @RequestMapping(value = "/itemInformations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemInformation> updateItemInformation(@Valid @RequestBody ItemInformation itemInformation) throws URISyntaxException {
        log.debug("REST request to update ItemInformation : {}", itemInformation);
        if (itemInformation.getId() == null) {
            return createItemInformation(itemInformation);
        }
        ItemInformation result = itemInformationRepository.save(itemInformation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemInformation", itemInformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itemInformations -> get all the itemInformations.
     */
    @RequestMapping(value = "/itemInformations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemInformation>> getAllItemInformations(Pageable pageable)
        throws URISyntaxException {
        Page<ItemInformation> page = itemInformationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemInformations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itemInformations/:id -> get the "id" itemInformation.
     */
    @RequestMapping(value = "/itemInformations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemInformation> getItemInformation(@PathVariable Long id) {
        log.debug("REST request to get ItemInformation : {}", id);
        return Optional.ofNullable(itemInformationRepository.findOne(id))
            .map(itemInformation -> new ResponseEntity<>(
                itemInformation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itemInformations/:id -> delete the "id" itemInformation.
     */
    @RequestMapping(value = "/itemInformations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemInformation(@PathVariable Long id) {
        log.debug("REST request to delete ItemInformation : {}", id);
        itemInformationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemInformation", id.toString())).build();
    }
}
