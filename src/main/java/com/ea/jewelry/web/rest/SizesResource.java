package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.Sizes;
import com.ea.jewelry.repository.SizesRepository;
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
 * REST controller for managing Sizes.
 */
@RestController
@RequestMapping("/api")
public class SizesResource {

    private final Logger log = LoggerFactory.getLogger(SizesResource.class);

    @Inject
    private SizesRepository sizesRepository;

    /**
     * POST  /sizess -> Create a new sizes.
     */
    @RequestMapping(value = "/sizess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sizes> createSizes(@Valid @RequestBody Sizes sizes) throws URISyntaxException {
        log.debug("REST request to save Sizes : {}", sizes);
        if (sizes.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sizes cannot already have an ID").body(null);
        }
        Sizes result = sizesRepository.save(sizes);
        return ResponseEntity.created(new URI("/api/sizess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sizes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sizess -> Updates an existing sizes.
     */
    @RequestMapping(value = "/sizess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sizes> updateSizes(@Valid @RequestBody Sizes sizes) throws URISyntaxException {
        log.debug("REST request to update Sizes : {}", sizes);
        if (sizes.getId() == null) {
            return createSizes(sizes);
        }
        Sizes result = sizesRepository.save(sizes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sizes", sizes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sizess -> get all the sizess.
     */
    @RequestMapping(value = "/sizess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sizes>> getAllSizess(Pageable pageable)
        throws URISyntaxException {
        Page<Sizes> page = sizesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sizess");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sizess/:id -> get the "id" sizes.
     */
    @RequestMapping(value = "/sizess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sizes> getSizes(@PathVariable Long id) {
        log.debug("REST request to get Sizes : {}", id);
        return Optional.ofNullable(sizesRepository.findOne(id))
            .map(sizes -> new ResponseEntity<>(
                sizes,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sizess/:id -> delete the "id" sizes.
     */
    @RequestMapping(value = "/sizess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSizes(@PathVariable Long id) {
        log.debug("REST request to delete Sizes : {}", id);
        sizesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sizes", id.toString())).build();
    }
}
