package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.Image;
import com.ea.jewelry.domain.Item;
import com.ea.jewelry.domain.ItemInformation;
import com.ea.jewelry.domain.Status;
import com.ea.jewelry.repository.ItemInformationRepository;
import com.ea.jewelry.repository.StatusRepository;
import com.ea.jewelry.service.ImageService;
import com.ea.jewelry.service.ListItemService;
import com.ea.jewelry.web.rest.util.FileManagementUtil;
import com.ea.jewelry.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

    @Inject
    private ImageService imageService;

    @Inject
    private StatusRepository statusRepository;


    /**
     * GET  /listItem -> get all the item information.
     */
    @RequestMapping(value = "/listItem/status",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemInformation>> getAllItemsFilterStatus(Pageable pageable, String filter)
        throws URISyntaxException {
        Status status = statusRepository.findOneByName(filter);
        Page<ItemInformation> page = itemInformationRepository.findAllByStatus(status, pageable);
        List<ItemInformation> result = listItemService.getItemInformationWithItemAndImages(page.getContent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/listItem/status");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * GET  /listItem -> get all the item information.
     */
    @RequestMapping(value = "/listItem",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemInformation>> getAllItems(Pageable pageable, String status)
        throws URISyntaxException {
//        Status status = statusRepository.findOneByName("Available");
//        Page<ItemInformation> page = itemInformationRepository.findAllByStatus(status, pageable);
        Page<ItemInformation> page = itemInformationRepository.findAll(pageable);
        List<ItemInformation> result = listItemService.getItemInformationWithItemAndImages(page.getContent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/listItem");
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

    /**
     * POST  /ListItem -> Create an Image for an Item.
     */
    @RequestMapping(value = "/listItem/upload/{id}", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Item> createImageWithItem(@RequestPart(value="file") MultipartFile file,
                                                    @PathVariable Long id,
                                                    HttpServletRequest request) {
        String applicationPath = request.getServletContext().getRealPath("");
        Image image = imageService.createImage();
        Item item = imageService.setImageToItem(image, id);
        item = cleanupItem(item);
        String itemDirectoryPath = FileManagementUtil.generateImageFolderWithItemPath(applicationPath, item.getId(), image.getId());
        boolean imageTransferStatus = FileManagementUtil.transferMultiPartToImages(file, itemDirectoryPath, "full");

        if (!imageTransferStatus) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(item);
    }

    @Transactional(readOnly = true)
    private Item cleanupItem(Item item) {
        if (item.getPrice() != null) {
            item.getPrice().setItems(null);
        }

        if(item.getItemInformation() != null && item.getItemInformation().getItems() != null){
            item.getItemInformation().setItems(null);
        }

        if(item.getImages() != null && item.getImages() != null) {
            item.getImages().stream().forEach(
                image -> {
                    image.setItem(null);
                });
        }

        return item;
    }

}
