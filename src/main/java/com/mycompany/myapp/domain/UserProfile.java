package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "points")
    private Integer points;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "user", "foodTruck" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @JsonIgnoreProperties(value = { "owner", "menuItems", "orders", "cuisineType", "location" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "owner")
    private FoodTruck truck;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public UserProfile email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return this.points;
    }

    public UserProfile points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setUser(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setUser(this));
        }
        this.reviews = reviews;
    }

    public UserProfile reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public UserProfile addReviews(Review review) {
        this.reviews.add(review);
        review.setUser(this);
        return this;
    }

    public UserProfile removeReviews(Review review) {
        this.reviews.remove(review);
        review.setUser(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setUser(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setUser(this));
        }
        this.orders = orders;
    }

    public UserProfile orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public UserProfile addOrders(Order order) {
        this.orders.add(order);
        order.setUser(this);
        return this;
    }

    public UserProfile removeOrders(Order order) {
        this.orders.remove(order);
        order.setUser(null);
        return this;
    }

    public FoodTruck getTruck() {
        return this.truck;
    }

    public void setTruck(FoodTruck foodTruck) {
        if (this.truck != null) {
            this.truck.setOwner(null);
        }
        if (foodTruck != null) {
            foodTruck.setOwner(this);
        }
        this.truck = foodTruck;
    }

    public UserProfile truck(FoodTruck foodTruck) {
        this.setTruck(foodTruck);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", points=" + getPoints() +
            "}";
    }
}
