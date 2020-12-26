package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Entry.
 */
@Entity
@Table(name = "entry")
public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne
    @JsonIgnoreProperties(value = "entries", allowSetters = true)
    private Forum forum;

    @ManyToMany
    @JoinTable(name = "entry_keyword",
               joinColumns = @JoinColumn(name = "entry_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "keyword_id", referencedColumnName = "id"))
    private Set<Keyword> keywords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Entry title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Entry content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getDate() {
        return date;
    }

    public Entry date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Forum getForum() {
        return forum;
    }

    public Entry forum(Forum forum) {
        this.forum = forum;
        return this;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public Entry keywords(Set<Keyword> keywords) {
        this.keywords = keywords;
        return this;
    }

    public Entry addKeyword(Keyword keyword) {
        this.keywords.add(keyword);
        keyword.getEntries().add(this);
        return this;
    }

    public Entry removeKeyword(Keyword keyword) {
        this.keywords.remove(keyword);
        keyword.getEntries().remove(this);
        return this;
    }

    public void setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entry)) {
            return false;
        }
        return id != null && id.equals(((Entry) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entry{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
