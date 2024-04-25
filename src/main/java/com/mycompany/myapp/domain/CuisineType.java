package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CuisineType.
 */
@Entity
@Table(name = "cuisine_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CuisineType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cuisineType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "menuItems", "orders", "cuisineType", "location" }, allowSetters = true)
    private Set<FoodTruck> trucks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CuisineType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CuisineType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FoodTruck> getTrucks() {
        return this.trucks;
    }

    public void setTrucks(Set<FoodTruck> foodTrucks) {
        if (this.trucks != null) {
            this.trucks.forEach(i -> i.setCuisineType(null));
        }
        if (foodTrucks != null) {
            foodTrucks.forEach(i -> i.setCuisineType(this));
        }
        this.trucks = foodTrucks;
    }

    public CuisineType trucks(Set<FoodTruck> foodTrucks) {
        this.setTrucks(foodTrucks);
        return this;
    }

    public CuisineType addTrucks(FoodTruck foodTruck) {
        this.trucks.add(foodTruck);
        foodTruck.setCuisineType(this);
        return this;
    }

    public CuisineType removeTrucks(FoodTruck foodTruck) {
        this.trucks.remove(foodTruck);
        foodTruck.setCuisineType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CuisineType)) {
            return false;
        }
        return getId() != null && getId().equals(((CuisineType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuisineType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
