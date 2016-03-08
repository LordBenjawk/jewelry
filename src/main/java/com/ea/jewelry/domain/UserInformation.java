package com.ea.jewelry.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserInformation.
 */
@Entity
@Table(name = "user_information")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserInformation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "price_tier")
    private Integer priceTier;

    @Column(name = "vip")
    private Boolean vip;

    @OneToOne    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriceTier() {
        return priceTier;
    }

    public void setPriceTier(Integer priceTier) {
        this.priceTier = priceTier;
    }

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInformation userInformation = (UserInformation) o;
        return Objects.equals(id, userInformation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserInformation{" +
            "id=" + id +
            ", priceTier='" + priceTier + "'" +
            ", vip='" + vip + "'" +
            '}';
    }
}
