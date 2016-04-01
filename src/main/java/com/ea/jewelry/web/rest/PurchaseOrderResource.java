package com.ea.jewelry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ea.jewelry.domain.PurchaseOrder;
import com.ea.jewelry.domain.PurchaseOrderDetails;
import com.ea.jewelry.repository.PurchaseOrderDetailsRepository;
import com.ea.jewelry.repository.PurchaseOrderRepository;
import com.ea.jewelry.service.PurchaseOrderService;
import com.ea.jewelry.service.ShoppingCartService;
import com.ea.jewelry.web.rest.dto.ShoppingCartCustomerDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing PurchaseOrder.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderResource.class);

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

    @Inject
    private PurchaseOrderService purchaseOrderService;

    @Inject
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    @Inject
    private ShoppingCartService shoppingCartService;

    /**
     * POST  /purchaseOrders -> Create a new purchaseOrder.
     */
    @RequestMapping(value = "/purchaseOrders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrder : {}", purchaseOrder);
        if (purchaseOrder.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new purchaseOrder cannot already have an ID").body(null);
        }
        purchaseOrder.setCreatedAt(new Date());
        PurchaseOrder result = purchaseOrderRepository.save(purchaseOrder);
        return ResponseEntity.created(new URI("/api/purchaseOrders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("purchaseOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchaseOrders -> Updates an existing purchaseOrder.
     */
    @RequestMapping(value = "/purchaseOrders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrder : {}", purchaseOrder);
        if (purchaseOrder.getId() == null) {
            return createPurchaseOrder(purchaseOrder);
        }
        PurchaseOrder result = purchaseOrderRepository.save(purchaseOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("purchaseOrder", purchaseOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchaseOrders -> get all the purchaseOrders.
     */
    @RequestMapping(value = "/purchaseOrders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(Pageable pageable)
        throws URISyntaxException {
        Page<PurchaseOrder> page = purchaseOrderRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchaseOrders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /purchaseOrders/:id -> get the "id" purchaseOrder.
     */
    @RequestMapping(value = "/purchaseOrders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrder : {}", id);
        return Optional.ofNullable(purchaseOrderRepository.findOne(id))
            .map(purchaseOrder -> new ResponseEntity<>(
                purchaseOrder,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /purchaseOrders/:id -> delete the "id" purchaseOrder.
     */
    @RequestMapping(value = "/purchaseOrders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrder : {}", id);
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(id);
        Set<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAllByPurchaseOrder(purchaseOrder);
        purchaseOrderDetailsRepository.delete(purchaseOrderDetailsList);
        purchaseOrderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("purchaseOrder", id.toString())).build();
    }

    /**
     * POST  /purchaseOrders/placeOrder -> Create a new purchaseOrder with Shopping Cart.
     */
    @RequestMapping(value = "/purchaseOrders/placeOrder",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrder> placeOrder(@RequestBody ShoppingCartCustomerDTO shoppingCartCustomerDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrder : {}", shoppingCartCustomerDTO);
        PurchaseOrder result = purchaseOrderService.generatePurchaseOrderFromShoppingCart(shoppingCartCustomerDTO);
        if (result != null) {
            shoppingCartService.cleanShoppingCart();
        }
        return ResponseEntity.created(new URI("/api/purchaseOrders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("purchaseOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchaseOrders/:id -> get the "id" purchaseOrder.
     */
    @RequestMapping(value = "/purchaseOrders/print/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PurchaseOrder> printPurchaseOrder(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrder : {}", id);
        return Optional.ofNullable(purchaseOrderRepository.findOne(id))
            .map(purchaseOrder -> new ResponseEntity<>(
                purchaseOrder,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public static final String DEST = "results/tables/simple_table.pdf";
    /**
     * GET  /purchaseOrders/:id -> get the "id" purchaseOrder.
     */
    @RequestMapping(value = "/purchaseOrders/test/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public void test(@PathVariable Long id,HttpServletRequest request) {
        log.debug("Test print PDF: {}", id);
        String applicationPath = request.getServletContext().getRealPath("");

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(id);
        Set<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAllByPurchaseOrder(purchaseOrder);
        purchaseOrder.setPurchaseOrderDetailss(purchaseOrderDetailsList);
        boolean isReportValid = purchaseOrderService.generatePurchaseOrderReports(applicationPath, purchaseOrder);
        log.debug(isReportValid + "");
    }
}
