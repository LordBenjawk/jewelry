package com.ea.jewelry.service.helper;

import com.ea.jewelry.domain.Price;

public class ItemPriceHelper {

    public static Double getPriceWithPriceTier(Price price, int priceTier) {
        Double actualPrice;
        switch (priceTier) {
            case 1:
                actualPrice =  price.getTierOne();
                break;
            case 2:
                actualPrice =  price.getTierTwo();
                break;
            case 3:
                actualPrice =  price.getTierThree();
                break;
            default:
                actualPrice =  0.0;
        }
        return actualPrice;
    }
}
