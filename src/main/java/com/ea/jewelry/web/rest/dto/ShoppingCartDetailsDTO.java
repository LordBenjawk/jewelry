package com.ea.jewelry.web.rest.dto;

import com.ea.jewelry.domain.Item;

public class ShoppingCartDetailsDTO {
    private Item item;
    private Integer quantity;

    public ShoppingCartDetailsDTO() {
    }

    public ShoppingCartDetailsDTO(Item item) {
        this.item = item;
        quantity = 1;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void addItemQuantity() {
        quantity++;
    }
}
