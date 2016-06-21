package com.ea.jewelry.service;

import com.ea.jewelry.domain.User;
import com.ea.jewelry.repository.UserInformationRepository;
import com.ea.jewelry.service.helper.PurchaseOrderPDFHelper;
import com.ea.jewelry.web.rest.dto.PurchaseOrderDetailsReportDTO;
import com.ea.jewelry.web.rest.dto.PurchaseOrderReportDTO;
import com.ea.jewelry.web.rest.util.FileManagementUtil;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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

    @Inject
    private UserInformationRepository userInformationRepository;

    @Inject
    private PurchaseOrderPDFHelper purchaseOrderPDFHelper;

    public PurchaseOrderPDFService() {
    }

    public void setPurchaseOrderReportsPath(String purchaseOrderReportsPath) {
        this.purchaseOrderReportsPath = purchaseOrderReportsPath;
    }

    public void setPurchaseOrderReportDTO(PurchaseOrderReportDTO purchaseOrderReportDTO) {
        this.purchaseOrderReportDTO = purchaseOrderReportDTO;
    }

    private boolean createReport(boolean price,
                                 boolean allVendors,
                                 String reportType) throws IOException, DocumentException {
        String vendorName =
            this.purchaseOrderReportDTO.getUserInformation().getUser().getLastName() +
            this.purchaseOrderReportDTO.getUserInformation().getUser().getFirstName();
        int vendor = this.purchaseOrderReportDTO.getUserInformation().getUser().getId().intValue();
        PurchaseOrderReportDTO pOrder = this.purchaseOrderReportDTO;
        List<User> vendors = getPurchaseOrderVendors();
//        Document pdf = purchaseOrderPDFHelper.getDocument();

//        PdfWriter.getInstance(pdf, new FileOutputStream(purchaseOrderReportsPath + reportType));
        purchaseOrderPDFHelper.evaluator(price, allVendors, vendor, vendorName, pOrder, vendors, new FileOutputStream(purchaseOrderReportsPath + reportType));
        if (FileManagementUtil.directoryExists(purchaseOrderReportsPath + reportType)) {
            return true;
        }
        return false;
    }

    public boolean createAdminReport() throws IOException, DocumentException {
        String reportType = File.separator + ADMIN_REPORT;
        boolean result = createReport(true, true, reportType);
        if (result && FileManagementUtil.directoryExists(purchaseOrderReportsPath + reportType)) {
            return true;
        }
        return false;
    }

    public boolean createVendorReport() throws IOException, DocumentException {
        String reportType = File.separator + VENDOR_REPORT;
        boolean result = createReport(false, false, reportType);
        if (result && FileManagementUtil.directoryExists(purchaseOrderReportsPath + reportType)) {
            return true;
        }
        return false;
    }

    public boolean createAdminSeparatedVendorReport() throws IOException, DocumentException {
        String reportType = File.separator + ADMIN_SEPARATED_VENDOR_REPORT;
        boolean result = createReport(false, true, reportType);
        if (result && FileManagementUtil.directoryExists(purchaseOrderReportsPath + reportType)) {
            return true;
        }
        return false;
    }

    public boolean createCustomerReport() throws IOException, DocumentException {
        return true;
    }

    private List<User> getPurchaseOrderVendors() {
        int poItemSize = purchaseOrderReportDTO.getPurchaseOrderDetailsReportDTOList().size();
        PurchaseOrderDetailsReportDTO itemDetails = null;
        List<User> vendors = new ArrayList<>();

        for(int i = 0; i < poItemSize; i++) {
            itemDetails = purchaseOrderReportDTO.getPurchaseOrderDetailsReportDTOList().get(i);
            User user = itemDetails.getItem().getItemInformation().getUser();

            if (!vendors.contains(user)) {
                vendors.add(user);
            }
        }
        return vendors;
    }
}
