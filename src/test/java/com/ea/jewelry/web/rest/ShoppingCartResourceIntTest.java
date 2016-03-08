package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.ShoppingCart;
import com.ea.jewelry.repository.ShoppingCartRepository;

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
 * Test class for the ShoppingCartResource REST controller.
 *
 * @see ShoppingCartResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ShoppingCartResourceIntTest {


    @Inject
    private ShoppingCartRepository shoppingCartRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShoppingCartMockMvc;

    private ShoppingCart shoppingCart;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShoppingCartResource shoppingCartResource = new ShoppingCartResource();
        ReflectionTestUtils.setField(shoppingCartResource, "shoppingCartRepository", shoppingCartRepository);
        this.restShoppingCartMockMvc = MockMvcBuilders.standaloneSetup(shoppingCartResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shoppingCart = new ShoppingCart();
    }

    @Test
    @Transactional
    public void createShoppingCart() throws Exception {
        int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

        // Create the ShoppingCart

        restShoppingCartMockMvc.perform(post("/api/shoppingCarts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
                .andExpect(status().isCreated());

        // Validate the ShoppingCart in the database
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        assertThat(shoppingCarts).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingCart testShoppingCart = shoppingCarts.get(shoppingCarts.size() - 1);
    }

    @Test
    @Transactional
    public void getAllShoppingCarts() throws Exception {
        // Initialize the database
        shoppingCartRepository.saveAndFlush(shoppingCart);

        // Get all the shoppingCarts
        restShoppingCartMockMvc.perform(get("/api/shoppingCarts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingCart.getId().intValue())));
    }

    @Test
    @Transactional
    public void getShoppingCart() throws Exception {
        // Initialize the database
        shoppingCartRepository.saveAndFlush(shoppingCart);

        // Get the shoppingCart
        restShoppingCartMockMvc.perform(get("/api/shoppingCarts/{id}", shoppingCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shoppingCart.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShoppingCart() throws Exception {
        // Get the shoppingCart
        restShoppingCartMockMvc.perform(get("/api/shoppingCarts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingCart() throws Exception {
        // Initialize the database
        shoppingCartRepository.saveAndFlush(shoppingCart);

		int databaseSizeBeforeUpdate = shoppingCartRepository.findAll().size();

        // Update the shoppingCart

        restShoppingCartMockMvc.perform(put("/api/shoppingCarts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shoppingCart)))
                .andExpect(status().isOk());

        // Validate the ShoppingCart in the database
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        assertThat(shoppingCarts).hasSize(databaseSizeBeforeUpdate);
        ShoppingCart testShoppingCart = shoppingCarts.get(shoppingCarts.size() - 1);
    }

    @Test
    @Transactional
    public void deleteShoppingCart() throws Exception {
        // Initialize the database
        shoppingCartRepository.saveAndFlush(shoppingCart);

		int databaseSizeBeforeDelete = shoppingCartRepository.findAll().size();

        // Get the shoppingCart
        restShoppingCartMockMvc.perform(delete("/api/shoppingCarts/{id}", shoppingCart.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        assertThat(shoppingCarts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
