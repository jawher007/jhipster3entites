package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Forum.
 */
@Entity
@Table(name = "forum")
public class Forum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 2)
    @Column(name = "handle", nullable = false)
    private String handle;

    @ManyToOne
    @JsonIgnoreProperties(value = "forums", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Forum name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public Forum handle(String handle) {
        this.handle = handle;
        return this;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public User getUser() {
        return user;
    }

    public Forum user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forum)) {
            return false;
        }
        return id != null && id.equals(((Forum) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Forum{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", handle='" + getHandle() + "'" +
            "}";
    }
}
