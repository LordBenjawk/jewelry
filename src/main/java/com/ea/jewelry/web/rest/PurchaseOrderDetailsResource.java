package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.PurchaseOrderDetails;
import com.ea.jewelry.repository.PurchaseOrderDetailsRepository;
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
 * REST controller for managing PurchaseOrderDetails.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsResource.class);

    @Inject
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    /**
     * POST  /purchaseOrderDetailss -> Create a new purchaseOrderDetails.
     */
    @RequestMapping(value = "/purchaseOrderDetailss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrderDetails> createPurchaseOrderDetails(@RequestBody PurchaseOrderDetails purchaseOrderDetails) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderDetails : {}", purchaseOrderDetails);
        if (purchaseOrderDetails.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new purchaseOrderDetails cannot already have an ID").body(null);
        }
        PurchaseOrderDetails result = purchaseOrderDetailsRepository.save(purchaseOrderDetails);
        return ResponseEntity.created(new URI("/api/purchaseOrderDetailss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("purchaseOrderDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchaseOrderDetailss -> Updates an existing purchaseOrderDetails.
     */
    @RequestMapping(value = "/purchaseOrderDetailss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrderDetails> updatePurchaseOrderDetails(@RequestBody PurchaseOrderDetails purchaseOrderDetails) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderDetails : {}", purchaseOrderDetails);
        if (purchaseOrderDetails.getId() == null) {
            return createPurchaseOrderDetails(purchaseOrderDetails);
        }
        PurchaseOrderDetails result = purchaseOrderDetailsRepository.save(purchaseOrderDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("purchaseOrderDetails", purchaseOrderDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchaseOrderDetailss -> get all the purchaseOrderDetailss.
     */
    @RequestMapping(value = "/purchaseOrderDetailss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PurchaseOrderDetails>> getAllPurchaseOrderDetailss(Pageable pageable)
        throws URISyntaxException {
        Page<PurchaseOrderDetails> page = purchaseOrderDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchaseOrderDetailss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /purchaseOrderDetailss/:id -> get the "id" purchaseOrderDetails.
     */
    @RequestMapping(value = "/purchaseOrderDetailss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrderDetails> getPurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderDetails : {}", id);
        return Optional.ofNullable(purchaseOrderDetailsRepository.findOne(id))
            .map(purchaseOrderDetails -> new ResponseEntity<>(
                purchaseOrderDetails,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /purchaseOrderDetailss/:id -> delete the "id" purchaseOrderDetails.
     */
    @RequestMapping(value = "/purchaseOrderDetailss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderDetails : {}", id);
        purchaseOrderDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("purchaseOrderDetails", id.toString())).build();
    }
}
