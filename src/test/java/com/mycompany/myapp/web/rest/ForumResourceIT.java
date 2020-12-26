package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Forum;
import com.mycompany.myapp.repository.ForumRepository;

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
 * Integration tests for the {@link ForumResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WithMockUser
public class ForumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HANDLE = "AAAAAAAAAA";
    private static final String UPDATED_HANDLE = "BBBBBBBBBB";

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restForumMockMvc;

    private Forum forum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forum createEntity(EntityManager em) {
        Forum forum = new Forum()
            .name(DEFAULT_NAME)
            .handle(DEFAULT_HANDLE);
        return forum;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forum createUpdatedEntity(EntityManager em) {
        Forum forum = new Forum()
            .name(UPDATED_NAME)
            .handle(UPDATED_HANDLE);
        return forum;
    }

    @BeforeEach
    public void initTest() {
        forum = createEntity(em);
    }

    @Test
    @Transactional
    public void createForum() throws Exception {
        int databaseSizeBeforeCreate = forumRepository.findAll().size();
        // Create the Forum
        restForumMockMvc.perform(post("/api/forums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isCreated());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeCreate + 1);
        Forum testForum = forumList.get(forumList.size() - 1);
        assertThat(testForum.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testForum.getHandle()).isEqualTo(DEFAULT_HANDLE);
    }

    @Test
    @Transactional
    public void createForumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = forumRepository.findAll().size();

        // Create the Forum with an existing ID
        forum.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restForumMockMvc.perform(post("/api/forums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isBadRequest());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = forumRepository.findAll().size();
        // set the field null
        forum.setName(null);

        // Create the Forum, which fails.


        restForumMockMvc.perform(post("/api/forums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isBadRequest());

        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHandleIsRequired() throws Exception {
        int databaseSizeBeforeTest = forumRepository.findAll().size();
        // set the field null
        forum.setHandle(null);

        // Create the Forum, which fails.


        restForumMockMvc.perform(post("/api/forums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isBadRequest());

        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllForums() throws Exception {
        // Initialize the database
        forumRepository.saveAndFlush(forum);

        // Get all the forumList
        restForumMockMvc.perform(get("/api/forums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forum.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].handle").value(hasItem(DEFAULT_HANDLE)));
    }
    
    @Test
    @Transactional
    public void getForum() throws Exception {
        // Initialize the database
        forumRepository.saveAndFlush(forum);

        // Get the forum
        restForumMockMvc.perform(get("/api/forums/{id}", forum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(forum.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.handle").value(DEFAULT_HANDLE));
    }
    @Test
    @Transactional
    public void getNonExistingForum() throws Exception {
        // Get the forum
        restForumMockMvc.perform(get("/api/forums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateForum() throws Exception {
        // Initialize the database
        forumRepository.saveAndFlush(forum);

        int databaseSizeBeforeUpdate = forumRepository.findAll().size();

        // Update the forum
        Forum updatedForum = forumRepository.findById(forum.getId()).get();
        // Disconnect from session so that the updates on updatedForum are not directly saved in db
        em.detach(updatedForum);
        updatedForum
            .name(UPDATED_NAME)
            .handle(UPDATED_HANDLE);

        restForumMockMvc.perform(put("/api/forums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedForum)))
            .andExpect(status().isOk());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeUpdate);
        Forum testForum = forumList.get(forumList.size() - 1);
        assertThat(testForum.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testForum.getHandle()).isEqualTo(UPDATED_HANDLE);
    }

    @Test
    @Transactional
    public void updateNonExistingForum() throws Exception {
        int databaseSizeBeforeUpdate = forumRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restForumMockMvc.perform(put("/api/forums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isBadRequest());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteForum() throws Exception {
        // Initialize the database
        forumRepository.saveAndFlush(forum);

        int databaseSizeBeforeDelete = forumRepository.findAll().size();

        // Delete the forum
        restForumMockMvc.perform(delete("/api/forums/{id}", forum.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
