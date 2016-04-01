package com.ea.jewelry.service;

import com.ea.jewelry.service.helper.PurchaseOrderPDFHelper;
import com.ea.jewelry.service.helper.PurchaseOrderReportType;
import com.ea.jewelry.web.rest.dto.PurchaseOrderReportDTO;
import com.ea.jewelry.web.rest.util.FileManagementUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class PurchaseOrderPDFService {
    private final Logger log = LoggerFactory.getLogger(PurchaseOrderPDFService.class);
    private static final String DOT = ".";
    private static final String PDF_EXTENSION = "pdf";
    private static final String ADMIN_SEPARATED_VENDOR_REPORT = "admin_separated_vendor" + DOT + PDF_EXTENSION;
    private static final String ADMIN_REPORT = "admin" + DOT + PDF_EXTENSION;
    private static final String VENDOR_REPORT = "vendor" + DOT + PDF_EXTENSION;
    private static final String CUSTOMER_REPORT = "customer" + DOT + PDF_EXTENSION;;
    private String purchaseOrderReportsPath;
    private PurchaseOrderReportDTO purchaseOrderReportDTO;

    public PurchaseOrderPDFService() {
    }

    public PurchaseOrderPDFService(String purchaseOrderReportsPath, PurchaseOrderReportDTO purchaseOrderReportDTO) {
        this.purchaseOrderReportsPath = purchaseOrderReportsPath;
        this.purchaseOrderReportDTO = purchaseOrderReportDTO;
    }

    public String getPurchaseOrderReportsPath() {
        return purchaseOrderReportsPath;
    }

    public void setPurchaseOrderReportsPath(String purchaseOrderReportsPath) {
        this.purchaseOrderReportsPath = purchaseOrderReportsPath;
    }

    public PurchaseOrderReportDTO getPurchaseOrderReportDTO() {
        return purchaseOrderReportDTO;
    }

    public void setPurchaseOrderReportDTO(PurchaseOrderReportDTO purchaseOrderReportDTO) {
        this.purchaseOrderReportDTO = purchaseOrderReportDTO;
    }

    public boolean createAdminReport() {
        PurchaseOrderPDFHelper purchaseOrderPDFHelper;
        String poNumber = purchaseOrderReportDTO.getPurchaseOrderNumber();
        String orderDate = purchaseOrderReportDTO.getPurchaseOrderDate();
        String status = purchaseOrderReportDTO.getStatus();
        boolean isValid = Boolean.FALSE;
        String reportPath = purchaseOrderReportsPath + ADMIN_REPORT;
        File file = FileManagementUtil.createDirectoryAndFile(purchaseOrderReportsPath , ADMIN_REPORT);
        Document document = new Document();

        String cutomerName = purchaseOrderReportDTO.getUserInformation().getUser().getLastName() +
            purchaseOrderReportDTO.getUserInformation().getUser().getFirstName();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(reportPath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        purchaseOrderPDFHelper = new PurchaseOrderPDFHelper(document, PurchaseOrderReportType.ADMIN);
        purchaseOrderPDFHelper.open();
        purchaseOrderPDFHelper.setTitle("Purchase Order");
        purchaseOrderPDFHelper.setHeaderInformation("customer", "vendor");

        purchaseOrderPDFHelper.setHeaderPurchaseOrderInformation(poNumber, orderDate, status);
        purchaseOrderPDFHelper.setBodyTable(purchaseOrderReportDTO);
        purchaseOrderPDFHelper.close();

//        isValid = Boolean.TRUE;
        return isValid;
    }

    public boolean createVendorReport() {
        return true;
    }

    public boolean createAdminSeparatedVendorReport() {
        return true;
    }

    public boolean createCustomerReport() {
        return true;
    }
}
