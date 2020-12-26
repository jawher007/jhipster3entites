package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Keyword;
import com.mycompany.myapp.repository.KeywordRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Keyword}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KeywordResource {

    private final Logger log = LoggerFactory.getLogger(KeywordResource.class);

    private static final String ENTITY_NAME = "keyword";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KeywordRepository keywordRepository;

    public KeywordResource(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    /**
     * {@code POST  /keywords} : Create a new keyword.
     *
     * @param keyword the keyword to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keyword, or with status {@code 400 (Bad Request)} if the keyword has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/keywords")
    public ResponseEntity<Keyword> createKeyword(@Valid @RequestBody Keyword keyword) throws URISyntaxException {
        log.debug("REST request to save Keyword : {}", keyword);
        if (keyword.getId() != null) {
            throw new BadRequestAlertException("A new keyword cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Keyword result = keywordRepository.save(keyword);
        return ResponseEntity.created(new URI("/api/keywords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /keywords} : Updates an existing keyword.
     *
     * @param keyword the keyword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyword,
     * or with status {@code 400 (Bad Request)} if the keyword is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keyword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/keywords")
    public ResponseEntity<Keyword> updateKeyword(@Valid @RequestBody Keyword keyword) throws URISyntaxException {
        log.debug("REST request to update Keyword : {}", keyword);
        if (keyword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Keyword result = keywordRepository.save(keyword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keyword.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /keywords} : get all the keywords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keywords in body.
     */
    @GetMapping("/keywords")
    public List<Keyword> getAllKeywords() {
        log.debug("REST request to get all Keywords");
        return keywordRepository.findAll();
    }

    /**
     * {@code GET  /keywords/:id} : get the "id" keyword.
     *
     * @param id the id of the keyword to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keyword, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/keywords/{id}")
    public ResponseEntity<Keyword> getKeyword(@PathVariable Long id) {
        log.debug("REST request to get Keyword : {}", id);
        Optional<Keyword> keyword = keywordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(keyword);
    }

    /**
     * {@code DELETE  /keywords/:id} : delete the "id" keyword.
     *
     * @param id the id of the keyword to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/keywords/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long id) {
        log.debug("REST request to delete Keyword : {}", id);
        keywordRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
