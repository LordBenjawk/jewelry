package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.PurchaseOrderDetails;
import com.ea.jewelry.repository.PurchaseOrderDetailsRepository;

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
 * Test class for the PurchaseOrderDetailsResource REST controller.
 *
 * @see PurchaseOrderDetailsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PurchaseOrderDetailsResourceIntTest {


    @Inject
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPurchaseOrderDetailsMockMvc;

    private PurchaseOrderDetails purchaseOrderDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PurchaseOrderDetailsResource purchaseOrderDetailsResource = new PurchaseOrderDetailsResource();
        ReflectionTestUtils.setField(purchaseOrderDetailsResource, "purchaseOrderDetailsRepository", purchaseOrderDetailsRepository);
        this.restPurchaseOrderDetailsMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        purchaseOrderDetails = new PurchaseOrderDetails();
    }

    @Test
    @Transactional
    public void createPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();

        // Create the PurchaseOrderDetails

        restPurchaseOrderDetailsMockMvc.perform(post("/api/purchaseOrderDetailss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails)))
                .andExpect(status().isCreated());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailss = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailss).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailss.get(purchaseOrderDetailss.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderDetailss() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailss
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchaseOrderDetailss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderDetails.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchaseOrderDetailss/{id}", purchaseOrderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(purchaseOrderDetails.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseOrderDetails() throws Exception {
        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(get("/api/purchaseOrderDetailss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

		int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails

        restPurchaseOrderDetailsMockMvc.perform(put("/api/purchaseOrderDetailss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails)))
                .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailss = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailss).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailss.get(purchaseOrderDetailss.size() - 1);
    }

    @Test
    @Transactional
    public void deletePurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

		int databaseSizeBeforeDelete = purchaseOrderDetailsRepository.findAll().size();

        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(delete("/api/purchaseOrderDetailss/{id}", purchaseOrderDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseOrderDetails> purchaseOrderDetailss = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
