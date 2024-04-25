package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MenuItem.
 */
@Entity
@Table(name = "menu_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "owner", "menuItems", "orders", "cuisineType", "location" }, allowSetters = true)
    private FoodTruck truck;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "menuItems")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "user", "foodTruck" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MenuItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MenuItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MenuItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return this.price;
    }

    public MenuItem price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public FoodTruck getTruck() {
        return this.truck;
    }

    public void setTruck(FoodTruck foodTruck) {
        this.truck = foodTruck;
    }

    public MenuItem truck(FoodTruck foodTruck) {
        this.setTruck(foodTruck);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.removeMenuItem(this));
        }
        if (orders != null) {
            orders.forEach(i -> i.addMenuItem(this));
        }
        this.orders = orders;
    }

    public MenuItem orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public MenuItem addOrders(Order order) {
        this.orders.add(order);
        order.getMenuItems().add(this);
        return this;
    }

    public MenuItem removeOrders(Order order) {
        this.orders.remove(order);
        order.getMenuItems().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItem)) {
            return false;
        }
        return getId() != null && getId().equals(((MenuItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
