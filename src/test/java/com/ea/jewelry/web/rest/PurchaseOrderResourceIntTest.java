package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.PurchaseOrder;
import com.ea.jewelry.repository.PurchaseOrderRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PurchaseOrderResource REST controller.
 *
 * @see PurchaseOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PurchaseOrderResourceIntTest {

    private static final String DEFAULT_PURCHASE_ORDER_NUMBER = "AAAAA";
    private static final String UPDATED_PURCHASE_ORDER_NUMBER = "BBBBB";

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPurchaseOrderMockMvc;

    private PurchaseOrder purchaseOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PurchaseOrderResource purchaseOrderResource = new PurchaseOrderResource();
        ReflectionTestUtils.setField(purchaseOrderResource, "purchaseOrderRepository", purchaseOrderRepository);
        this.restPurchaseOrderMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderNumber(DEFAULT_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void createPurchaseOrder() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

        // Create the PurchaseOrder

        restPurchaseOrderMockMvc.perform(post("/api/purchaseOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
                .andExpect(status().isCreated());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        assertThat(purchaseOrders).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrder testPurchaseOrder = purchaseOrders.get(purchaseOrders.size() - 1);
        assertThat(testPurchaseOrder.getPurchaseOrderNumber()).isEqualTo(DEFAULT_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrders
        restPurchaseOrderMockMvc.perform(get("/api/purchaseOrders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get("/api/purchaseOrders/{id}", purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(purchaseOrder.getId().intValue()))
            .andExpect(jsonPath("$.purchaseOrderNumber").value(DEFAULT_PURCHASE_ORDER_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseOrder() throws Exception {
        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get("/api/purchaseOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

		int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder
        purchaseOrder.setPurchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER);

        restPurchaseOrderMockMvc.perform(put("/api/purchaseOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
                .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        assertThat(purchaseOrders).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrders.get(purchaseOrders.size() - 1);
        assertThat(testPurchaseOrder.getPurchaseOrderNumber()).isEqualTo(UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void deletePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

		int databaseSizeBeforeDelete = purchaseOrderRepository.findAll().size();

        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(delete("/api/purchaseOrders/{id}", purchaseOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        assertThat(purchaseOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
