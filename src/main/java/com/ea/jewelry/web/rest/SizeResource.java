package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.Size;
import com.ea.jewelry.repository.SizeRepository;
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
 * REST controller for managing Size.
 */
@RestController
@RequestMapping("/api")
public class SizeResource {

    private final Logger log = LoggerFactory.getLogger(SizeResource.class);

    @Inject
    private SizeRepository sizeRepository;

    /**
     * POST  /sizes -> Create a new size.
     */
    @RequestMapping(value = "/sizes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Size> createSize(@Valid @RequestBody Size size) throws URISyntaxException {
        log.debug("REST request to save Size : {}", size);
        if (size.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new size cannot already have an ID").body(null);
        }
        Size result = sizeRepository.save(size);
        return ResponseEntity.created(new URI("/api/sizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("size", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sizes -> Updates an existing size.
     */
    @RequestMapping(value = "/sizes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Size> updateSize(@Valid @RequestBody Size size) throws URISyntaxException {
        log.debug("REST request to update Size : {}", size);
        if (size.getId() == null) {
            return createSize(size);
        }
        Size result = sizeRepository.save(size);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("size", size.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sizes -> get all the sizes.
     */
    @RequestMapping(value = "/sizes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Size>> getAllSizes(Pageable pageable)
        throws URISyntaxException {
        Page<Size> page = sizeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sizes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sizes/:id -> get the "id" size.
     */
    @RequestMapping(value = "/sizes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Size> getSize(@PathVariable Long id) {
        log.debug("REST request to get Size : {}", id);
        return Optional.ofNullable(sizeRepository.findOne(id))
            .map(size -> new ResponseEntity<>(
                size,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sizes/:id -> delete the "id" size.
     */
    @RequestMapping(value = "/sizes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSize(@PathVariable Long id) {
        log.debug("REST request to delete Size : {}", id);
        sizeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("size", id.toString())).build();
    }
}
