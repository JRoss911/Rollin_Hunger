package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "menuItems", "orders", "cuisineType", "location" }, allowSetters = true)
    private Set<FoodTruck> trucks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public Location address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Location latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public Location longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Set<FoodTruck> getTrucks() {
        return this.trucks;
    }

    public void setTrucks(Set<FoodTruck> foodTrucks) {
        if (this.trucks != null) {
            this.trucks.forEach(i -> i.setLocation(null));
        }
        if (foodTrucks != null) {
            foodTrucks.forEach(i -> i.setLocation(this));
        }
        this.trucks = foodTrucks;
    }

    public Location trucks(Set<FoodTruck> foodTrucks) {
        this.setTrucks(foodTrucks);
        return this;
    }

    public Location addTrucks(FoodTruck foodTruck) {
        this.trucks.add(foodTruck);
        foodTruck.setLocation(this);
        return this;
    }

    public Location removeTrucks(FoodTruck foodTruck) {
        this.trucks.remove(foodTruck);
        foodTruck.setLocation(null);
        return this;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.setLocation(null));
        }
        if (events != null) {
            events.forEach(i -> i.setLocation(this));
        }
        this.events = events;
    }

    public Location events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public Location addEvents(Event event) {
        this.events.add(event);
        event.setLocation(this);
        return this;
    }

    public Location removeEvents(Event event) {
        this.events.remove(event);
        event.setLocation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return getId() != null && getId().equals(((Location) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
