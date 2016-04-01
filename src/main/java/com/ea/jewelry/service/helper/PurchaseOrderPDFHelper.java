package com.ea.jewelry.service.helper;

import com.ea.jewelry.domain.Item;
import com.ea.jewelry.web.rest.dto.PurchaseOrderDetailsReportDTO;
import com.ea.jewelry.web.rest.dto.PurchaseOrderReportDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PurchaseOrderPDFHelper {
    private final Logger log = LoggerFactory.getLogger(PurchaseOrderPDFHelper.class);

    private BaseColor titleBackground = BaseColor.GRAY;
    private Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
    private Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.WHITE);
    private BaseColor textBackground = BaseColor.WHITE;
    private Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private Document document;
    private PurchaseOrderReportType purchaseOrderReportType;


    public PurchaseOrderPDFHelper(Document document, PurchaseOrderReportType purchaseOrderReportType) {
        this.document = document;
        this.purchaseOrderReportType = purchaseOrderReportType;
    }

    public void createHeader() {

    }

    public void createBody() {

    }

    public void createFooter() {

    }


    /*
    Header
     */

    public void setTitle(String titleText) {
        Paragraph title = new Paragraph(titleText, headerFont);
        addElement(title);
        addSeparator();
    }

    public void setHeaderInformation(String customerName) {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(40);

        table.addCell(titleCell("Customer Name"));
        table.addCell(textCell(customerName));
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        addElement(table);
        addSeparator();
    }

    public void setHeaderInformation(String customerName, String vendorName) {
        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(40);

        table.addCell(titleCell("Customer Name"));
        table.addCell(titleCell("Vendor Name"));
        table.addCell(textCell(customerName));
        table.addCell(textCell(vendorName));
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        addElement(table);
        addSeparator();
    }

    public void setHeaderPurchaseOrderInformation(String purchaseOrderNumber, String orderDate, String status) {
        PdfPTable table = new PdfPTable(3);

        table.setWidthPercentage(40);

        table.addCell(titleCell("P.O Number"));
        table.addCell(titleCell("Order Date"));
        table.addCell(titleCell("Status"));
        table.addCell(textCell(purchaseOrderNumber));
        table.addCell(textCell(orderDate));
        table.addCell(textCell(status));
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        addElement(table);
        addSeparator();
    }

    /*
    Body
    */

    public void setBodyTable(PurchaseOrderReportDTO purchaseOrderReportDTO) {
        switch (purchaseOrderReportType) {
            case ADMIN:
                setBodyTableForAdmin(purchaseOrderReportDTO);
                setTotal(purchaseOrderReportDTO.getTotalPrice().toString());
                break;
        }
    }

    private void setBodyTableForAdmin(PurchaseOrderReportDTO purchaseOrderReportDTO) {
        int vendorTier = purchaseOrderReportDTO.getUserInformation().getPriceTier();
        List<PurchaseOrderDetailsReportDTO> purchaseOrderDetailsReportDTOList =
            purchaseOrderReportDTO.getPurchaseOrderDetailsReportDTOList();
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        table.addCell(titleCell("Item number"));
        table.addCell(titleCell("Size"));
        table.addCell(titleCell("Color"));
        table.addCell(titleCell("Quantity"));
        table.addCell(titleCell("Price"));
        table.addCell(titleCell("Subtotal"));

        purchaseOrderDetailsReportDTOList.forEach(
            purchaseOrderDetailsReportDTO -> {
                Item item = purchaseOrderDetailsReportDTO.getItem();
                String itemNumber = item.getId().toString();
                String size = item.getSize().getName();
                String color = item.getColor().getName();
                String quantity = purchaseOrderDetailsReportDTO.getQuantity().toString();
                String price = ItemPriceHelper.getPriceWithPriceTier(item.getPrice(),vendorTier).toString();
                String subtotal = purchaseOrderDetailsReportDTO.getSubtotal().toString();
                table.addCell(textCell(itemNumber));
                table.addCell(textCell(size));
                table.addCell(textCell(color));
                table.addCell(textCell(quantity));
                table.addCell(textCell(price));
                table.addCell(textCell(subtotal));
            }
        );
        addElement(table);
    }

    private void setTotal(String total) {
        PdfPTable table = new PdfPTable(6);
        PdfPCell cell = textCell("Total");
        table.setWidthPercentage(100);
        cell.setColspan(5);
        table.addCell(cell);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(textCell(total));
        addElement(table);
    }

    /*
    Footer
     */

    public void setFooter() {

    }


    //////

    private PdfPCell titleCell(String content) {
        return createCell(titleBackground, titleFont, content);
    }

    private PdfPCell textCell(String content) {
        return createCell(textBackground, textFont, content);
    }

    private PdfPCell createCell(BaseColor baseColor, Font font, String content) {
        Paragraph contentText = new Paragraph(content,font);
        contentText.setLeading(0,1);
        PdfPCell cell = new PdfPCell();
        cell.addElement(contentText);
        cell.setMinimumHeight(20f);
        cell.setBackgroundColor(baseColor);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }


    private void addSeparator() {
        Paragraph paragraph = new Paragraph("\u00a0", titleFont);
        addElement(paragraph);
    }


    public void addElement(Element element) {
        try {
            document.add(element);
        } catch (Exception e) {
            log.debug("Error adding element:" + element);
        }
    }

    public void open() {
        this.document.open();
    }

    public void close() {
        this.document.close();
    }

}
