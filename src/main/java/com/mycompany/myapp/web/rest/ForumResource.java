package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Forum;
import com.mycompany.myapp.repository.ForumRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Forum}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ForumResource {

    private final Logger log = LoggerFactory.getLogger(ForumResource.class);

    private static final String ENTITY_NAME = "forum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ForumRepository forumRepository;

    public ForumResource(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    /**
     * {@code POST  /forums} : Create a new forum.
     *
     * @param forum the forum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new forum, or with status {@code 400 (Bad Request)} if the forum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forums")
    public ResponseEntity<Forum> createForum(@Valid @RequestBody Forum forum) throws URISyntaxException {
        log.debug("REST request to save Forum : {}", forum);
        if (forum.getId() != null) {
            throw new BadRequestAlertException("A new forum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Forum result = forumRepository.save(forum);
        return ResponseEntity.created(new URI("/api/forums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forums} : Updates an existing forum.
     *
     * @param forum the forum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forum,
     * or with status {@code 400 (Bad Request)} if the forum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the forum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forums")
    public ResponseEntity<Forum> updateForum(@Valid @RequestBody Forum forum) throws URISyntaxException {
        log.debug("REST request to update Forum : {}", forum);
        if (forum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Forum result = forumRepository.save(forum);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, forum.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /forums} : get all the forums.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of forums in body.
     */
    @GetMapping("/forums")
    public List<Forum> getAllForums() {
        log.debug("REST request to get all Forums");
        return forumRepository.findAll();
    }

    /**
     * {@code GET  /forums/:id} : get the "id" forum.
     *
     * @param id the id of the forum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the forum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forums/{id}")
    public ResponseEntity<Forum> getForum(@PathVariable Long id) {
        log.debug("REST request to get Forum : {}", id);
        Optional<Forum> forum = forumRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(forum);
    }

    /**
     * {@code DELETE  /forums/:id} : delete the "id" forum.
     *
     * @param id the id of the forum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forums/{id}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long id) {
        log.debug("REST request to delete Forum : {}", id);
        forumRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
