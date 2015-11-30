package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.ItemInformation;
import com.ea.jewelry.repository.ItemInformationRepository;

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
 * Test class for the ItemInformationResource REST controller.
 *
 * @see ItemInformationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemInformationResourceIntTest {

    private static final String DEFAULT_ITEM_NUMBER = "";
    private static final String UPDATED_ITEM_NUMBER = "";
    private static final String DEFAULT_VENDOR_ITEM_NUMBER = "";
    private static final String UPDATED_VENDOR_ITEM_NUMBER = "";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_VIP = false;
    private static final Boolean UPDATED_VIP = true;

    @Inject
    private ItemInformationRepository itemInformationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemInformationMockMvc;

    private ItemInformation itemInformation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemInformationResource itemInformationResource = new ItemInformationResource();
        ReflectionTestUtils.setField(itemInformationResource, "itemInformationRepository", itemInformationRepository);
        this.restItemInformationMockMvc = MockMvcBuilders.standaloneSetup(itemInformationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemInformation = new ItemInformation();
        itemInformation.setItemNumber(DEFAULT_ITEM_NUMBER);
        itemInformation.setVendorItemNumber(DEFAULT_VENDOR_ITEM_NUMBER);
        itemInformation.setDescription(DEFAULT_DESCRIPTION);
        itemInformation.setActive(DEFAULT_ACTIVE);
        itemInformation.setVip(DEFAULT_VIP);
    }

    @Test
    @Transactional
    public void createItemInformation() throws Exception {
        int databaseSizeBeforeCreate = itemInformationRepository.findAll().size();

        // Create the ItemInformation

        restItemInformationMockMvc.perform(post("/api/itemInformations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemInformation)))
                .andExpect(status().isCreated());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformations = itemInformationRepository.findAll();
        assertThat(itemInformations).hasSize(databaseSizeBeforeCreate + 1);
        ItemInformation testItemInformation = itemInformations.get(itemInformations.size() - 1);
        assertThat(testItemInformation.getItemNumber()).isEqualTo(DEFAULT_ITEM_NUMBER);
        assertThat(testItemInformation.getVendorItemNumber()).isEqualTo(DEFAULT_VENDOR_ITEM_NUMBER);
        assertThat(testItemInformation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItemInformation.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testItemInformation.getVip()).isEqualTo(DEFAULT_VIP);
    }

    @Test
    @Transactional
    public void checkItemNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemInformationRepository.findAll().size();
        // set the field null
        itemInformation.setItemNumber(null);

        // Create the ItemInformation, which fails.

        restItemInformationMockMvc.perform(post("/api/itemInformations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemInformation)))
                .andExpect(status().isBadRequest());

        List<ItemInformation> itemInformations = itemInformationRepository.findAll();
        assertThat(itemInformations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVendorItemNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemInformationRepository.findAll().size();
        // set the field null
        itemInformation.setVendorItemNumber(null);

        // Create the ItemInformation, which fails.

        restItemInformationMockMvc.perform(post("/api/itemInformations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemInformation)))
                .andExpect(status().isBadRequest());

        List<ItemInformation> itemInformations = itemInformationRepository.findAll();
        assertThat(itemInformations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemInformations() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        // Get all the itemInformations
        restItemInformationMockMvc.perform(get("/api/itemInformations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemInformation.getId().intValue())))
                .andExpect(jsonPath("$.[*].itemNumber").value(hasItem(DEFAULT_ITEM_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].vendorItemNumber").value(hasItem(DEFAULT_VENDOR_ITEM_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].vip").value(hasItem(DEFAULT_VIP.booleanValue())));
    }

    @Test
    @Transactional
    public void getItemInformation() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        // Get the itemInformation
        restItemInformationMockMvc.perform(get("/api/itemInformations/{id}", itemInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemInformation.getId().intValue()))
            .andExpect(jsonPath("$.itemNumber").value(DEFAULT_ITEM_NUMBER.toString()))
            .andExpect(jsonPath("$.vendorItemNumber").value(DEFAULT_VENDOR_ITEM_NUMBER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.vip").value(DEFAULT_VIP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItemInformation() throws Exception {
        // Get the itemInformation
        restItemInformationMockMvc.perform(get("/api/itemInformations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemInformation() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

		int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();

        // Update the itemInformation
        itemInformation.setItemNumber(UPDATED_ITEM_NUMBER);
        itemInformation.setVendorItemNumber(UPDATED_VENDOR_ITEM_NUMBER);
        itemInformation.setDescription(UPDATED_DESCRIPTION);
        itemInformation.setActive(UPDATED_ACTIVE);
        itemInformation.setVip(UPDATED_VIP);

        restItemInformationMockMvc.perform(put("/api/itemInformations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemInformation)))
                .andExpect(status().isOk());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformations = itemInformationRepository.findAll();
        assertThat(itemInformations).hasSize(databaseSizeBeforeUpdate);
        ItemInformation testItemInformation = itemInformations.get(itemInformations.size() - 1);
        assertThat(testItemInformation.getItemNumber()).isEqualTo(UPDATED_ITEM_NUMBER);
        assertThat(testItemInformation.getVendorItemNumber()).isEqualTo(UPDATED_VENDOR_ITEM_NUMBER);
        assertThat(testItemInformation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemInformation.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testItemInformation.getVip()).isEqualTo(UPDATED_VIP);
    }

    @Test
    @Transactional
    public void deleteItemInformation() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

		int databaseSizeBeforeDelete = itemInformationRepository.findAll().size();

        // Get the itemInformation
        restItemInformationMockMvc.perform(delete("/api/itemInformations/{id}", itemInformation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemInformation> itemInformations = itemInformationRepository.findAll();
        assertThat(itemInformations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
