package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private String status;

    @Column(name = "timestamp")
    private Instant timestamp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_jhi_order__menu_item",
        joinColumns = @JoinColumn(name = "jhi_order_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "truck", "orders" }, allowSetters = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviews", "orders", "truck" }, allowSetters = true)
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "owner", "menuItems", "orders", "cuisineType", "location" }, allowSetters = true)
    private FoodTruck foodTruck;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Order quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return this.status;
    }

    public Order status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Order timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Set<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Order menuItems(Set<MenuItem> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public Order addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        return this;
    }

    public Order removeMenuItem(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public Order user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public FoodTruck getFoodTruck() {
        return this.foodTruck;
    }

    public void setFoodTruck(FoodTruck foodTruck) {
        this.foodTruck = foodTruck;
    }

    public Order foodTruck(FoodTruck foodTruck) {
        this.setFoodTruck(foodTruck);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", status='" + getStatus() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
