package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FoodTruck.
 */
@Entity
@Table(name = "food_truck")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FoodTruck implements Serializable {

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

    @Column(name = "rating")
    private Float rating;

    @Column(name = "profile_picture")
    private String profilePicture;

    @JsonIgnoreProperties(value = { "reviews", "orders", "truck" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private UserProfile owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "truck")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "truck", "orders" }, allowSetters = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "foodTruck")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "user", "foodTruck" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trucks" }, allowSetters = true)
    private CuisineType cuisineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trucks", "events" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FoodTruck id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FoodTruck name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public FoodTruck description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getRating() {
        return this.rating;
    }

    public FoodTruck rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public FoodTruck profilePicture(String profilePicture) {
        this.setProfilePicture(profilePicture);
        return this;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public UserProfile getOwner() {
        return this.owner;
    }

    public void setOwner(UserProfile userProfile) {
        this.owner = userProfile;
    }

    public FoodTruck owner(UserProfile userProfile) {
        this.setOwner(userProfile);
        return this;
    }

    public Set<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        if (this.menuItems != null) {
            this.menuItems.forEach(i -> i.setTruck(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setTruck(this));
        }
        this.menuItems = menuItems;
    }

    public FoodTruck menuItems(Set<MenuItem> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public FoodTruck addMenuItems(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setTruck(this);
        return this;
    }

    public FoodTruck removeMenuItems(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setTruck(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setFoodTruck(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setFoodTruck(this));
        }
        this.orders = orders;
    }

    public FoodTruck orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public FoodTruck addOrders(Order order) {
        this.orders.add(order);
        order.setFoodTruck(this);
        return this;
    }

    public FoodTruck removeOrders(Order order) {
        this.orders.remove(order);
        order.setFoodTruck(null);
        return this;
    }

    public CuisineType getCuisineType() {
        return this.cuisineType;
    }

    public void setCuisineType(CuisineType cuisineType) {
        this.cuisineType = cuisineType;
    }

    public FoodTruck cuisineType(CuisineType cuisineType) {
        this.setCuisineType(cuisineType);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public FoodTruck location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodTruck)) {
            return false;
        }
        return getId() != null && getId().equals(((FoodTruck) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodTruck{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", rating=" + getRating() +
            ", profilePicture='" + getProfilePicture() + "'" +
            "}";
    }
}
