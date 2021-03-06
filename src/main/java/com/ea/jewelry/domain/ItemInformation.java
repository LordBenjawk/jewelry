package com.ea.jewelry.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A ItemInformation.
 */
@Entity
@Table(name = "item_information")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemInformation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "item_number", nullable = false)
    private String itemNumber;

    @Column(name = "vendor_item_number")
    private String vendorItemNumber;

    @Column(name = "description")
    private String description;

//    @NotNull
    @Column(name = "vip")
    private Boolean vip;

    @OneToMany(mappedBy = "itemInformation")
//    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "min_month")
    private String minMonth;

    public String getMinMonth() {
        return minMonth;
    }

    public void setMinMonth(String minMonth) {
        this.minMonth = minMonth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getVendorItemNumber() {
        return vendorItemNumber;
    }

    public void setVendorItemNumber(String vendorItemNumber) {
        this.vendorItemNumber = vendorItemNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemInformation itemInformation = (ItemInformation) o;
        return Objects.equals(id, itemInformation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItemInformation{" +
            "id=" + id +
            ", itemNumber='" + itemNumber + "'" +
            ", vendorItemNumber='" + vendorItemNumber + "'" +
            ", description='" + description + "'" +
            ", vip='" + vip + "'" +
            '}';
    }
}
