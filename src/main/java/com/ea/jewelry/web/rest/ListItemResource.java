package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.ItemInformation;
import com.ea.jewelry.repository.ItemInformationRepository;
import com.ea.jewelry.service.ListItemService;
import com.ea.jewelry.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing the ListItem view.
 */
@RestController
@RequestMapping("/api")
public class ListItemResource {
    private final Logger log = LoggerFactory.getLogger(ListItemResource.class);

    @Inject
    private ItemInformationRepository itemInformationRepository;

    @Inject
    private ListItemService listItemService;

    /**
     * GET  /listItem -> get all the item information.
     */
    @RequestMapping(value = "/listItem",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemInformation>> getAllItems(Pageable pageable)
        throws URISyntaxException {
        Page<ItemInformation> page = itemInformationRepository.findAll(pageable);
        List<ItemInformation> result = listItemService.getItemInformationWithItemAndImages(page.getContent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itemInformation");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET  /itemInformations/:id -> get the "id" itemInformation.
     */
    @RequestMapping(value = "/listItem/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemInformation> getItemInformation(@PathVariable Long id) {
        log.debug("REST request to get ItemInformation : {}", id);
        ItemInformation itemInformation = itemInformationRepository.findOne(id);
        itemInformation = listItemService.getItemInformationWithItemAndImages(itemInformation);

        if (itemInformation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(itemInformation,HttpStatus.OK);
    }

//    /**
//     * GET  /listItems/{id} -> get all the items.
//     */
//    @RequestMapping(value = "/listItems/{id}",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<List<Item>> getAllItems(@PathVariable Long id)
//        throws URISyntaxException {
//        log.debug("REST request to get ListItem : {}", id);
//        Item itemToGetItemInformation = itemRepository.findOne(id);
//        Long itemInformationId = itemToGetItemInformation.getItemInformation().getId();
//        ItemInformation itemInformation = itemInformationRepository.findOne(itemInformationId);
//        List<Item> listItem = itemRepository.findAllByItemInformation(itemInformation);
//        listItem.forEach(item->addImages(item));
//        return new ResponseEntity<>(listItem, HttpStatus.OK);
//    }
//
//    private Item addImages(Item item) {
//        Set<Image> images = imageRepository.findAllByItem(item);
//        item.setImages(images);
//
//        return item;
//    }
}
