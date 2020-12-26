package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Keyword;
import com.mycompany.myapp.repository.KeywordRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link KeywordResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WithMockUser
public class KeywordResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeywordMockMvc;

    private Keyword keyword;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Keyword createEntity(EntityManager em) {
        Keyword keyword = new Keyword()
            .name(DEFAULT_NAME);
        return keyword;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Keyword createUpdatedEntity(EntityManager em) {
        Keyword keyword = new Keyword()
            .name(UPDATED_NAME);
        return keyword;
    }

    @BeforeEach
    public void initTest() {
        keyword = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeyword() throws Exception {
        int databaseSizeBeforeCreate = keywordRepository.findAll().size();
        // Create the Keyword
        restKeywordMockMvc.perform(post("/api/keywords")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isCreated());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeCreate + 1);
        Keyword testKeyword = keywordList.get(keywordList.size() - 1);
        assertThat(testKeyword.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createKeywordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keywordRepository.findAll().size();

        // Create the Keyword with an existing ID
        keyword.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeywordMockMvc.perform(post("/api/keywords")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isBadRequest());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = keywordRepository.findAll().size();
        // set the field null
        keyword.setName(null);

        // Create the Keyword, which fails.


        restKeywordMockMvc.perform(post("/api/keywords")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isBadRequest());

        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKeywords() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

        // Get all the keywordList
        restKeywordMockMvc.perform(get("/api/keywords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getKeyword() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

        // Get the keyword
        restKeywordMockMvc.perform(get("/api/keywords/{id}", keyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keyword.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingKeyword() throws Exception {
        // Get the keyword
        restKeywordMockMvc.perform(get("/api/keywords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeyword() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

        int databaseSizeBeforeUpdate = keywordRepository.findAll().size();

        // Update the keyword
        Keyword updatedKeyword = keywordRepository.findById(keyword.getId()).get();
        // Disconnect from session so that the updates on updatedKeyword are not directly saved in db
        em.detach(updatedKeyword);
        updatedKeyword
            .name(UPDATED_NAME);

        restKeywordMockMvc.perform(put("/api/keywords")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedKeyword)))
            .andExpect(status().isOk());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeUpdate);
        Keyword testKeyword = keywordList.get(keywordList.size() - 1);
        assertThat(testKeyword.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingKeyword() throws Exception {
        int databaseSizeBeforeUpdate = keywordRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeywordMockMvc.perform(put("/api/keywords")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(keyword)))
            .andExpect(status().isBadRequest());

        // Validate the Keyword in the database
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKeyword() throws Exception {
        // Initialize the database
        keywordRepository.saveAndFlush(keyword);

        int databaseSizeBeforeDelete = keywordRepository.findAll().size();

        // Delete the keyword
        restKeywordMockMvc.perform(delete("/api/keywords/{id}", keyword.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Keyword> keywordList = keywordRepository.findAll();
        assertThat(keywordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
