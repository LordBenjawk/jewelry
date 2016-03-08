package com.ea.jewelry.web.rest;

import com.ea.jewelry.Application;
import com.ea.jewelry.domain.UserInformation;
import com.ea.jewelry.repository.UserInformationRepository;

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
 * Test class for the UserInformationResource REST controller.
 *
 * @see UserInformationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserInformationResourceIntTest {


    private static final Integer DEFAULT_PRICE_TIER = 1;
    private static final Integer UPDATED_PRICE_TIER = 2;

    private static final Boolean DEFAULT_VIP = false;
    private static final Boolean UPDATED_VIP = true;

    @Inject
    private UserInformationRepository userInformationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserInformationMockMvc;

    private UserInformation userInformation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserInformationResource userInformationResource = new UserInformationResource();
        ReflectionTestUtils.setField(userInformationResource, "userInformationRepository", userInformationRepository);
        this.restUserInformationMockMvc = MockMvcBuilders.standaloneSetup(userInformationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userInformation = new UserInformation();
        userInformation.setPriceTier(DEFAULT_PRICE_TIER);
        userInformation.setVip(DEFAULT_VIP);
    }

    @Test
    @Transactional
    public void createUserInformation() throws Exception {
        int databaseSizeBeforeCreate = userInformationRepository.findAll().size();

        // Create the UserInformation

        restUserInformationMockMvc.perform(post("/api/userInformations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInformation)))
                .andExpect(status().isCreated());

        // Validate the UserInformation in the database
        List<UserInformation> userInformations = userInformationRepository.findAll();
        assertThat(userInformations).hasSize(databaseSizeBeforeCreate + 1);
        UserInformation testUserInformation = userInformations.get(userInformations.size() - 1);
        assertThat(testUserInformation.getPriceTier()).isEqualTo(DEFAULT_PRICE_TIER);
        assertThat(testUserInformation.getVip()).isEqualTo(DEFAULT_VIP);
    }

    @Test
    @Transactional
    public void getAllUserInformations() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        // Get all the userInformations
        restUserInformationMockMvc.perform(get("/api/userInformations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userInformation.getId().intValue())))
                .andExpect(jsonPath("$.[*].priceTier").value(hasItem(DEFAULT_PRICE_TIER)))
                .andExpect(jsonPath("$.[*].vip").value(hasItem(DEFAULT_VIP.booleanValue())));
    }

    @Test
    @Transactional
    public void getUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        // Get the userInformation
        restUserInformationMockMvc.perform(get("/api/userInformations/{id}", userInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userInformation.getId().intValue()))
            .andExpect(jsonPath("$.priceTier").value(DEFAULT_PRICE_TIER))
            .andExpect(jsonPath("$.vip").value(DEFAULT_VIP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInformation() throws Exception {
        // Get the userInformation
        restUserInformationMockMvc.perform(get("/api/userInformations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

		int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();

        // Update the userInformation
        userInformation.setPriceTier(UPDATED_PRICE_TIER);
        userInformation.setVip(UPDATED_VIP);

        restUserInformationMockMvc.perform(put("/api/userInformations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userInformation)))
                .andExpect(status().isOk());

        // Validate the UserInformation in the database
        List<UserInformation> userInformations = userInformationRepository.findAll();
        assertThat(userInformations).hasSize(databaseSizeBeforeUpdate);
        UserInformation testUserInformation = userInformations.get(userInformations.size() - 1);
        assertThat(testUserInformation.getPriceTier()).isEqualTo(UPDATED_PRICE_TIER);
        assertThat(testUserInformation.getVip()).isEqualTo(UPDATED_VIP);
    }

    @Test
    @Transactional
    public void deleteUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

		int databaseSizeBeforeDelete = userInformationRepository.findAll().size();

        // Get the userInformation
        restUserInformationMockMvc.perform(delete("/api/userInformations/{id}", userInformation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInformation> userInformations = userInformationRepository.findAll();
        assertThat(userInformations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
