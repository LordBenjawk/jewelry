package com.ea.jewelry.web.rest.dto;

import com.ea.jewelry.domain.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderReportDTO {
    private UserInformation userInformation;
    private List<PurchaseOrderDetailsReportDTO> purchaseOrderDetailsReportDTOList = new ArrayList<>();
    private Double totalPrice;
    private String purchaseOrderNumber;
    private String purchaseOrderDate;
    private String status;

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public List<PurchaseOrderDetailsReportDTO> getPurchaseOrderDetailsReportDTOList() {
        return purchaseOrderDetailsReportDTOList;
    }

    public void setPurchaseOrderDetailsReportDTOList(List<PurchaseOrderDetailsReportDTO> purchaseOrderDetailsReportDTOList) {
        this.purchaseOrderDetailsReportDTOList = purchaseOrderDetailsReportDTOList;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(String purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
