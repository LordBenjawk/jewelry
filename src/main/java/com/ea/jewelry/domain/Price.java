package com.ea.jewelry.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Price implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "tier_one", nullable = false)
    private Double tierOne;

    @NotNull
    @Column(name = "tier_two", nullable = false)
    private Double tierTwo;

    @NotNull
    @Column(name = "tier_three", nullable = false)
    private Double tierThree;

    @NotNull
    @Column(name = "vendor_price", nullable = false)
    private Double vendorPrice;

    @OneToMany(mappedBy = "price")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Item> items = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTierOne() {
        return tierOne;
    }

    public void setTierOne(Double tierOne) {
        this.tierOne = tierOne;
    }

    public Double getTierTwo() {
        return tierTwo;
    }

    public void setTierTwo(Double tierTwo) {
        this.tierTwo = tierTwo;
    }

    public Double getTierThree() {
        return tierThree;
    }

    public void setTierThree(Double tierThree) {
        this.tierThree = tierThree;
    }

    public Double getVendorPrice() {
        return vendorPrice;
    }

    public void setVendorPrice(Double vendorPrice) {
        this.vendorPrice = vendorPrice;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(id, price.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Price{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", tierOne='" + tierOne + "'" +
            ", tierTwo='" + tierTwo + "'" +
            ", tierThree='" + tierThree + "'" +
            ", vendorPrice='" + vendorPrice + "'" +
            '}';
    }
}
