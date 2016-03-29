package com.ea.jewelry.service;

import com.ea.jewelry.domain.*;
import com.ea.jewelry.repository.PurchaseOrderDetailsRepository;
import com.ea.jewelry.repository.PurchaseOrderRepository;
import com.ea.jewelry.repository.StatusRepository;
import com.ea.jewelry.web.rest.dto.ShoppingCartCustomerDTO;
import com.ea.jewelry.web.rest.dto.ShoppingCartDetailsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PurchaseOrderService {

    @Inject
    private StatusRepository statusRepository;

    @Inject
    private PurchaseOrderRepository purchaseOrderRepository;

    @Inject
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    private final String statusNew = "Available";

    public PurchaseOrder generatePurchaseOrderFromShoppingCart(ShoppingCartCustomerDTO shoppingCartCustomerDTO) {
        User user = shoppingCartCustomerDTO.getUserInformation().getUser();
        Status status = statusRepository.findOneByName(statusNew);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Set<PurchaseOrderDetails> purchaseOrderDetailsSet =
            generatePurchaseOrderDetailsFromShoppingCart(shoppingCartCustomerDTO.getShoppingCartDetailsList(),purchaseOrder);
        purchaseOrder.setStatus(status);
        purchaseOrder.setUser(user);
        purchaseOrder.setPurchaseOrderDetailss(purchaseOrderDetailsSet);
        purchaseOrderRepository.save(purchaseOrder);
        purchaseOrderDetailsRepository.save(purchaseOrderDetailsSet);
        return purchaseOrder;
    }

    private Set<PurchaseOrderDetails> generatePurchaseOrderDetailsFromShoppingCart(List<ShoppingCartDetailsDTO> shoppingCartDetailsDTOList,
                                                                                   PurchaseOrder purchaseOrder) {
        Set<PurchaseOrderDetails> purchaseOrderDetailsSet = new HashSet<>();
        shoppingCartDetailsDTOList.forEach(shoppingCartDetailsDTO -> {
            int quantity = shoppingCartDetailsDTO.getQuantity();
            for(int i = 0; i < quantity; i++) {
                PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails();
                purchaseOrderDetails.setPurchaseOrder(purchaseOrder);
                Set<Item> items = new HashSet<>();
                items.add(shoppingCartDetailsDTO.getItem());
                purchaseOrderDetails.setItems(items);
                purchaseOrderDetails = purchaseOrderDetailsRepository.save(purchaseOrderDetails);
                purchaseOrderDetailsSet.add(purchaseOrderDetails);
            }
        });

        return purchaseOrderDetailsSet;
    }
}
