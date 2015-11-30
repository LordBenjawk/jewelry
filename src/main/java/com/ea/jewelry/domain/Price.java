package com.ea.jewelry.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
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

    @Column(name = "t_one")
    private Double tOne;

    @Column(name = "t_two")
    private Double tTwo;

    @Column(name = "t_three")
    private Double tThree;

    @Column(name = "vendor_price")
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

    public Double gettOne() {
        return tOne;
    }

    public void settOne(Double tOne) {
        this.tOne = tOne;
    }

    public Double gettTwo() {
        return tTwo;
    }

    public void settTwo(Double tTwo) {
        this.tTwo = tTwo;
    }

    public Double gettThree() {
        return tThree;
    }

    public void settThree(Double tThree) {
        this.tThree = tThree;
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
            ", tOne='" + tOne + "'" +
            ", tTwo='" + tTwo + "'" +
            ", tThree='" + tThree + "'" +
            ", vendorPrice='" + vendorPrice + "'" +
            '}';
    }
}
