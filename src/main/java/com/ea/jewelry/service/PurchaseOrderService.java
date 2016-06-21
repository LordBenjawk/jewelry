package com.ea.jewelry.service;

import com.ea.jewelry.domain.*;
import com.ea.jewelry.repository.PurchaseOrderDetailsRepository;
import com.ea.jewelry.repository.PurchaseOrderRepository;
import com.ea.jewelry.repository.StatusRepository;
import com.ea.jewelry.repository.UserInformationRepository;
import com.ea.jewelry.web.rest.dto.PurchaseOrderDetailsReportDTO;
import com.ea.jewelry.web.rest.dto.PurchaseOrderReportDTO;
import com.ea.jewelry.web.rest.dto.ShoppingCartCustomerDTO;
import com.ea.jewelry.web.rest.dto.ShoppingCartDetailsDTO;
import com.ea.jewelry.web.rest.util.FileManagementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

@Service
@Transactional
public class PurchaseOrderService {
    @Inject private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;
    @Inject private UserInformationRepository userInformationRepository;
    @Inject private PurchaseOrderPDFService purchaseOrderPDFService;
    @Inject private PurchaseOrderRepository purchaseOrderRepository;
    @Inject private StatusRepository statusRepository;
    private final String statusNew = "Available";
    private final Logger log = LoggerFactory.getLogger(PurchaseOrderService.class);

    public PurchaseOrder generatePurchaseOrderFromShoppingCart(ShoppingCartCustomerDTO shoppingCartCustomerDTO) {
        User user = shoppingCartCustomerDTO.getUserInformation().getUser();
        Status status = statusRepository.findOneByName(statusNew);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Set<PurchaseOrderDetails> purchaseOrderDetailsSet =
            generatePurchaseOrderDetailsFromShoppingCart(shoppingCartCustomerDTO.getShoppingCartDetailsList(),purchaseOrder);
        purchaseOrder.setStatus(status);
        purchaseOrder.setUser(user);
        purchaseOrder.setPurchaseOrderDetailss(purchaseOrderDetailsSet);
        purchaseOrder.setCreatedAt(new Date());
        purchaseOrderRepository.save(purchaseOrder);
        purchaseOrderDetailsRepository.save(purchaseOrderDetailsSet);
        return purchaseOrder;
    }


    @Transactional(readOnly = true)
    public boolean generatePurchaseOrderReports(String applicationPath, PurchaseOrder purchaseOrder) {
        PurchaseOrderReportDTO purchaseOrderReportDTO = mapToPurchaseOrderReportDTO(purchaseOrder);
        String purchaseOrderReportsPath = FileManagementUtil.generatePurchaseOrderReportPath(applicationPath);
        String purchaseOrderItemReportsPath = FileManagementUtil.generatePurchaseOrderReportPath(
            applicationPath,
            purchaseOrder.getId());
        boolean result = Boolean.FALSE,
            adminReport = Boolean.FALSE,
            vendorReport = Boolean.FALSE,
            adminSeparatedVendorReport = Boolean.FALSE;

        FileManagementUtil.createDirectory(purchaseOrderReportsPath);
        FileManagementUtil.createDirectory(purchaseOrderItemReportsPath);
        purchaseOrderPDFService.setPurchaseOrderReportsPath(purchaseOrderItemReportsPath);
        purchaseOrderPDFService.setPurchaseOrderReportDTO(purchaseOrderReportDTO);

        try{
            // G admin report
            adminReport = purchaseOrderPDFService.createAdminReport();
            // G vendor report
            vendorReport = purchaseOrderPDFService.createVendorReport();
            // G admin_vendor report
            adminSeparatedVendorReport = purchaseOrderPDFService.createAdminSeparatedVendorReport();
        } catch (Exception e) {
            log.debug("Error Purchase Order Report");
        }


        if (adminReport && vendorReport && adminSeparatedVendorReport) {
            result = Boolean.TRUE;
        }
        return result;
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

    private PurchaseOrderReportDTO mapToPurchaseOrderReportDTO(PurchaseOrder purchaseOrder) {
        UserInformation userInformation = userInformationRepository.findOneByUser(purchaseOrder.getUser());
        List<PurchaseOrderDetails> purchaseOrderDetailsList= new ArrayList<>();
        List<PurchaseOrderDetailsReportDTO> purchaseOrderDetailsReportDTOList;
        String status = purchaseOrder.getStatus().getName();
        String createdAt = purchaseOrder.getCreatedAt().toString().substring(0,10);

        purchaseOrderDetailsList.addAll(purchaseOrder.getPurchaseOrderDetailss());
        purchaseOrderDetailsReportDTOList = mapToPurchaseOrderDetailsToDTO(purchaseOrderDetailsList,userInformation);
        PurchaseOrderReportDTO purchaseOrderReportDTO = new PurchaseOrderReportDTO();
        purchaseOrderReportDTO.setUserInformation(userInformation);
        purchaseOrderReportDTO.setPurchaseOrderDetailsReportDTOList(purchaseOrderDetailsReportDTOList);

        Double totalPrice = 0.0;
        for (int i = 0; i < purchaseOrderDetailsReportDTOList.size(); i++) {
            totalPrice += purchaseOrderDetailsReportDTOList.get(i).getSubtotal();
        }
        purchaseOrderReportDTO.setTotalPrice(totalPrice);
        purchaseOrderReportDTO.setPurchaseOrderNumber(purchaseOrder.getId().toString());
        purchaseOrderReportDTO.setPurchaseOrderDate(createdAt);
        purchaseOrderReportDTO.setStatus(status);
        return purchaseOrderReportDTO;
    }

    private List<PurchaseOrderDetailsReportDTO> mapToPurchaseOrderDetailsToDTO(
                List<PurchaseOrderDetails> purchaseOrderDetailsList,
                UserInformation userInformation) {
        List<PurchaseOrderDetailsReportDTO> purchaseOrderDetailsReportDTOList = new ArrayList<>();
        purchaseOrderDetailsList.forEach(purchaseOrderDetails -> {
            Set<Item> itemSet = purchaseOrderDetails.getItems();
            Iterator iterator = itemSet.iterator();
            Item purchaseOrderItem = (Item) iterator.next();
            int priceTier = userInformation.getPriceTier();

            if (purchaseOrderDetailsReportDTOList.isEmpty()) {
                purchaseOrderDetailsReportDTOList.add(new PurchaseOrderDetailsReportDTO(purchaseOrderItem,priceTier));
            } else {
                int dtoListSize = purchaseOrderDetailsReportDTOList.size();
                boolean isItemEquals = Boolean.FALSE;
                for (int j = 0; j < dtoListSize; j++) {
                    PurchaseOrderDetailsReportDTO purchaseOrderDetailsReportDTO = purchaseOrderDetailsReportDTOList.get(j);
                    isItemEquals = purchaseOrderDetailsReportDTO.getItem().equals(purchaseOrderItem);

                    if(isItemEquals) {
                        purchaseOrderDetailsReportDTO.addItem();
                    }
                }

                if (!isItemEquals) {
                    purchaseOrderDetailsReportDTOList.add(new PurchaseOrderDetailsReportDTO(purchaseOrderItem,priceTier));
                }
            }
        });
        return purchaseOrderDetailsReportDTOList;
    }
}
