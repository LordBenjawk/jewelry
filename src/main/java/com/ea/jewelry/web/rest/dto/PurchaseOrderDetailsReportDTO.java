package com.ea.jewelry.web.rest.dto;

import com.ea.jewelry.domain.Item;
import com.ea.jewelry.domain.Price;
import com.ea.jewelry.service.helper.ItemPriceHelper;

public class PurchaseOrderDetailsReportDTO {
    private Item item;
    private Integer quantity;
    private Double subtotal = 0.0;
    private int priceTier;

    public PurchaseOrderDetailsReportDTO() {
    }

    public PurchaseOrderDetailsReportDTO(Item item, int priceTier) {
        this.item = item;
        this.quantity = 1;
        this.priceTier = priceTier;
        this.subtotal = calculateSubtotal();

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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public void addItem() {
        quantity++;
        subtotal = calculateSubtotal();
    }

    private Double calculateSubtotal() {
        Price price = item.getPrice();
        Double subtotal = this.subtotal + ItemPriceHelper.getPriceWithPriceTier(price,priceTier);
        return subtotal;
    }
}
