package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.Color;
import com.ea.jewelry.repository.ColorRepository;
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
 * REST controller for managing Color.
 */
@RestController
@RequestMapping("/api")
public class ColorResource {

    private final Logger log = LoggerFactory.getLogger(ColorResource.class);

    @Inject
    private ColorRepository colorRepository;

    /**
     * POST  /colors -> Create a new color.
     */
    @RequestMapping(value = "/colors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Color> createColor(@Valid @RequestBody Color color) throws URISyntaxException {
        log.debug("REST request to save Color : {}", color);
        if (color.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new color cannot already have an ID").body(null);
        }
        Color result = colorRepository.save(color);
        return ResponseEntity.created(new URI("/api/colors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("color", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /colors -> Updates an existing color.
     */
    @RequestMapping(value = "/colors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Color> updateColor(@Valid @RequestBody Color color) throws URISyntaxException {
        log.debug("REST request to update Color : {}", color);
        if (color.getId() == null) {
            return createColor(color);
        }
        Color result = colorRepository.save(color);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("color", color.getId().toString()))
            .body(result);
    }

    /**
     * GET  /colors -> get all the colors.
     */
    @RequestMapping(value = "/colors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Color>> getAllColors(Pageable pageable)
        throws URISyntaxException {
        Page<Color> page = colorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/colors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /colors/:id -> get the "id" color.
     */
    @RequestMapping(value = "/colors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Color> getColor(@PathVariable Long id) {
        log.debug("REST request to get Color : {}", id);
        return Optional.ofNullable(colorRepository.findOne(id))
            .map(color -> new ResponseEntity<>(
                color,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /colors/:id -> delete the "id" color.
     */
    @RequestMapping(value = "/colors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        log.debug("REST request to delete Color : {}", id);
        colorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("color", id.toString())).build();
    }
}
