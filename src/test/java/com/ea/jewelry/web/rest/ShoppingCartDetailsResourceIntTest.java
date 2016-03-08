package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.ShoppingCartDetails;
import com.ea.jewelry.repository.ShoppingCartDetailsRepository;

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
 * Test class for the ShoppingCartDetailsResource REST controller.
 *
 * @see ShoppingCartDetailsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ShoppingCartDetailsResourceIntTest {


    @Inject
    private ShoppingCartDetailsRepository shoppingCartDetailsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShoppingCartDetailsMockMvc;

    private ShoppingCartDetails shoppingCartDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShoppingCartDetailsResource shoppingCartDetailsResource = new ShoppingCartDetailsResource();
        ReflectionTestUtils.setField(shoppingCartDetailsResource, "shoppingCartDetailsRepository", shoppingCartDetailsRepository);
        this.restShoppingCartDetailsMockMvc = MockMvcBuilders.standaloneSetup(shoppingCartDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shoppingCartDetails = new ShoppingCartDetails();
    }

    @Test
    @Transactional
    public void createShoppingCartDetails() throws Exception {
        int databaseSizeBeforeCreate = shoppingCartDetailsRepository.findAll().size();

        // Create the ShoppingCartDetails

        restShoppingCartDetailsMockMvc.perform(post("/api/shoppingCartDetailss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shoppingCartDetails)))
                .andExpect(status().isCreated());

        // Validate the ShoppingCartDetails in the database
        List<ShoppingCartDetails> shoppingCartDetailss = shoppingCartDetailsRepository.findAll();
        assertThat(shoppingCartDetailss).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingCartDetails testShoppingCartDetails = shoppingCartDetailss.get(shoppingCartDetailss.size() - 1);
    }

    @Test
    @Transactional
    public void getAllShoppingCartDetailss() throws Exception {
        // Initialize the database
        shoppingCartDetailsRepository.saveAndFlush(shoppingCartDetails);

        // Get all the shoppingCartDetailss
        restShoppingCartDetailsMockMvc.perform(get("/api/shoppingCartDetailss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingCartDetails.getId().intValue())));
    }

    @Test
    @Transactional
    public void getShoppingCartDetails() throws Exception {
        // Initialize the database
        shoppingCartDetailsRepository.saveAndFlush(shoppingCartDetails);

        // Get the shoppingCartDetails
        restShoppingCartDetailsMockMvc.perform(get("/api/shoppingCartDetailss/{id}", shoppingCartDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shoppingCartDetails.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShoppingCartDetails() throws Exception {
        // Get the shoppingCartDetails
        restShoppingCartDetailsMockMvc.perform(get("/api/shoppingCartDetailss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingCartDetails() throws Exception {
        // Initialize the database
        shoppingCartDetailsRepository.saveAndFlush(shoppingCartDetails);

		int databaseSizeBeforeUpdate = shoppingCartDetailsRepository.findAll().size();

        // Update the shoppingCartDetails

        restShoppingCartDetailsMockMvc.perform(put("/api/shoppingCartDetailss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shoppingCartDetails)))
                .andExpect(status().isOk());

        // Validate the ShoppingCartDetails in the database
        List<ShoppingCartDetails> shoppingCartDetailss = shoppingCartDetailsRepository.findAll();
        assertThat(shoppingCartDetailss).hasSize(databaseSizeBeforeUpdate);
        ShoppingCartDetails testShoppingCartDetails = shoppingCartDetailss.get(shoppingCartDetailss.size() - 1);
    }

    @Test
    @Transactional
    public void deleteShoppingCartDetails() throws Exception {
        // Initialize the database
        shoppingCartDetailsRepository.saveAndFlush(shoppingCartDetails);

		int databaseSizeBeforeDelete = shoppingCartDetailsRepository.findAll().size();

        // Get the shoppingCartDetails
        restShoppingCartDetailsMockMvc.perform(delete("/api/shoppingCartDetailss/{id}", shoppingCartDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShoppingCartDetails> shoppingCartDetailss = shoppingCartDetailsRepository.findAll();
        assertThat(shoppingCartDetailss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
