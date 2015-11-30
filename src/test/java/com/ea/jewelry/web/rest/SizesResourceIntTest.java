package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.Sizes;
import com.ea.jewelry.repository.SizesRepository;

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
 * Test class for the SizesResource REST controller.
 *
 * @see SizesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SizesResourceIntTest {

    private static final String DEFAULT_NAME = "";
    private static final String UPDATED_NAME = "";

    @Inject
    private SizesRepository sizesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSizesMockMvc;

    private Sizes sizes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SizesResource sizesResource = new SizesResource();
        ReflectionTestUtils.setField(sizesResource, "sizesRepository", sizesRepository);
        this.restSizesMockMvc = MockMvcBuilders.standaloneSetup(sizesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sizes = new Sizes();
        sizes.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSizes() throws Exception {
        int databaseSizeBeforeCreate = sizesRepository.findAll().size();

        // Create the Sizes

        restSizesMockMvc.perform(post("/api/sizess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sizes)))
                .andExpect(status().isCreated());

        // Validate the Sizes in the database
        List<Sizes> sizess = sizesRepository.findAll();
        assertThat(sizess).hasSize(databaseSizeBeforeCreate + 1);
        Sizes testSizes = sizess.get(sizess.size() - 1);
        assertThat(testSizes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sizesRepository.findAll().size();
        // set the field null
        sizes.setName(null);

        // Create the Sizes, which fails.

        restSizesMockMvc.perform(post("/api/sizess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sizes)))
                .andExpect(status().isBadRequest());

        List<Sizes> sizess = sizesRepository.findAll();
        assertThat(sizess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSizess() throws Exception {
        // Initialize the database
        sizesRepository.saveAndFlush(sizes);

        // Get all the sizess
        restSizesMockMvc.perform(get("/api/sizess"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sizes.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSizes() throws Exception {
        // Initialize the database
        sizesRepository.saveAndFlush(sizes);

        // Get the sizes
        restSizesMockMvc.perform(get("/api/sizess/{id}", sizes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sizes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSizes() throws Exception {
        // Get the sizes
        restSizesMockMvc.perform(get("/api/sizess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSizes() throws Exception {
        // Initialize the database
        sizesRepository.saveAndFlush(sizes);

		int databaseSizeBeforeUpdate = sizesRepository.findAll().size();

        // Update the sizes
        sizes.setName(UPDATED_NAME);

        restSizesMockMvc.perform(put("/api/sizess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sizes)))
                .andExpect(status().isOk());

        // Validate the Sizes in the database
        List<Sizes> sizess = sizesRepository.findAll();
        assertThat(sizess).hasSize(databaseSizeBeforeUpdate);
        Sizes testSizes = sizess.get(sizess.size() - 1);
        assertThat(testSizes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSizes() throws Exception {
        // Initialize the database
        sizesRepository.saveAndFlush(sizes);

		int databaseSizeBeforeDelete = sizesRepository.findAll().size();

        // Get the sizes
        restSizesMockMvc.perform(delete("/api/sizess/{id}", sizes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sizes> sizess = sizesRepository.findAll();
        assertThat(sizess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
