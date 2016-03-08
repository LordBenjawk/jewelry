package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.Price;
import com.ea.jewelry.repository.PriceRepository;

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
 * Test class for the PriceResource REST controller.
 *
 * @see PriceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PriceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Double DEFAULT_TIER_ONE = 1D;
    private static final Double UPDATED_TIER_ONE = 2D;

    private static final Double DEFAULT_TIER_TWO = 1D;
    private static final Double UPDATED_TIER_TWO = 2D;

    private static final Double DEFAULT_TIER_THREE = 1D;
    private static final Double UPDATED_TIER_THREE = 2D;

    private static final Double DEFAULT_VENDOR_PRICE = 1D;
    private static final Double UPDATED_VENDOR_PRICE = 2D;

    @Inject
    private PriceRepository priceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPriceMockMvc;

    private Price price;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriceResource priceResource = new PriceResource();
        ReflectionTestUtils.setField(priceResource, "priceRepository", priceRepository);
        this.restPriceMockMvc = MockMvcBuilders.standaloneSetup(priceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        price = new Price();
        price.setName(DEFAULT_NAME);
        price.setTierOne(DEFAULT_TIER_ONE);
        price.setTierTwo(DEFAULT_TIER_TWO);
        price.setTierThree(DEFAULT_TIER_THREE);
        price.setVendorPrice(DEFAULT_VENDOR_PRICE);
    }

    @Test
    @Transactional
    public void createPrice() throws Exception {
        int databaseSizeBeforeCreate = priceRepository.findAll().size();

        // Create the Price

        restPriceMockMvc.perform(post("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isCreated());

        // Validate the Price in the database
        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeCreate + 1);
        Price testPrice = prices.get(prices.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrice.getTierOne()).isEqualTo(DEFAULT_TIER_ONE);
        assertThat(testPrice.getTierTwo()).isEqualTo(DEFAULT_TIER_TWO);
        assertThat(testPrice.getTierThree()).isEqualTo(DEFAULT_TIER_THREE);
        assertThat(testPrice.getVendorPrice()).isEqualTo(DEFAULT_VENDOR_PRICE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setName(null);

        // Create the Price, which fails.

        restPriceMockMvc.perform(post("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isBadRequest());

        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTierOneIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setTierOne(null);

        // Create the Price, which fails.

        restPriceMockMvc.perform(post("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isBadRequest());

        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTierTwoIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setTierTwo(null);

        // Create the Price, which fails.

        restPriceMockMvc.perform(post("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isBadRequest());

        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTierThreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setTierThree(null);

        // Create the Price, which fails.

        restPriceMockMvc.perform(post("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isBadRequest());

        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVendorPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setVendorPrice(null);

        // Create the Price, which fails.

        restPriceMockMvc.perform(post("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isBadRequest());

        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrices() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the prices
        restPriceMockMvc.perform(get("/api/prices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].tierOne").value(hasItem(DEFAULT_TIER_ONE.doubleValue())))
                .andExpect(jsonPath("$.[*].tierTwo").value(hasItem(DEFAULT_TIER_TWO.doubleValue())))
                .andExpect(jsonPath("$.[*].tierThree").value(hasItem(DEFAULT_TIER_THREE.doubleValue())))
                .andExpect(jsonPath("$.[*].vendorPrice").value(hasItem(DEFAULT_VENDOR_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get the price
        restPriceMockMvc.perform(get("/api/prices/{id}", price.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(price.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.tierOne").value(DEFAULT_TIER_ONE.doubleValue()))
            .andExpect(jsonPath("$.tierTwo").value(DEFAULT_TIER_TWO.doubleValue()))
            .andExpect(jsonPath("$.tierThree").value(DEFAULT_TIER_THREE.doubleValue()))
            .andExpect(jsonPath("$.vendorPrice").value(DEFAULT_VENDOR_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrice() throws Exception {
        // Get the price
        restPriceMockMvc.perform(get("/api/prices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

		int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price
        price.setName(UPDATED_NAME);
        price.setTierOne(UPDATED_TIER_ONE);
        price.setTierTwo(UPDATED_TIER_TWO);
        price.setTierThree(UPDATED_TIER_THREE);
        price.setVendorPrice(UPDATED_VENDOR_PRICE);

        restPriceMockMvc.perform(put("/api/prices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = prices.get(prices.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrice.getTierOne()).isEqualTo(UPDATED_TIER_ONE);
        assertThat(testPrice.getTierTwo()).isEqualTo(UPDATED_TIER_TWO);
        assertThat(testPrice.getTierThree()).isEqualTo(UPDATED_TIER_THREE);
        assertThat(testPrice.getVendorPrice()).isEqualTo(UPDATED_VENDOR_PRICE);
    }

    @Test
    @Transactional
    public void deletePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

		int databaseSizeBeforeDelete = priceRepository.findAll().size();

        // Get the price
        restPriceMockMvc.perform(delete("/api/prices/{id}", price.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Price> prices = priceRepository.findAll();
        assertThat(prices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
