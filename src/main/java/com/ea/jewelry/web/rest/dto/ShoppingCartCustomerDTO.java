package com.ea.jewelry.web.rest.dto;

import com.ea.jewelry.domain.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartCustomerDTO {
    private UserInformation userInformation;
    private List<ShoppingCartDetailsDTO> shoppingCartDetailsList = new ArrayList<>();
    private Double totalPrice;

    public ShoppingCartCustomerDTO() {}

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public List<ShoppingCartDetailsDTO> getShoppingCartDetailsList() {
        return shoppingCartDetailsList;
    }

    public void setShoppingCartDetailsList(List<ShoppingCartDetailsDTO> shoppingCartDetailsList) {
        this.shoppingCartDetailsList = shoppingCartDetailsList;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
