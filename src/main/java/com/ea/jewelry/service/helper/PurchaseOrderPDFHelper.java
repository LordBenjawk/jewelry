package com.ea.jewelry.service.helper;

import com.ea.jewelry.domain.User;
import com.ea.jewelry.domain.UserInformation;
import com.ea.jewelry.repository.UserInformationRepository;
import com.ea.jewelry.web.rest.dto.PurchaseOrderDetailsReportDTO;
import com.ea.jewelry.web.rest.dto.PurchaseOrderReportDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PurchaseOrderPDFHelper {
    private final Logger log = LoggerFactory.getLogger(PurchaseOrderPDFHelper.class);

    private UserInformationRepository userInformationRepository;
    private final String EMPTY_CHAR = "\u00a0",
                        SCORE_CHAR = "-",
                        PURCHASE_ORDER_TITLE = "PURCHASE ORDER";
    private final int ROWS_PER_PAGE = 30,
                        ADMIN_REPORT_TABLE_COLUMNS = 6,
                        CUSTOMER_REPORT_TABLE_COLUMNS = 4;

    private BaseColor titleBackground = BaseColor.GRAY;
    private Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
    private Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.WHITE);
    private BaseColor textBackground = BaseColor.WHITE;
    private Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private Document document;

    @Inject
    public PurchaseOrderPDFHelper(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    private void printMenu(Document pdf, String poNumber, String poDate, String poStatus, String customerName, String vendorName) {
        Paragraph title = new Paragraph(PURCHASE_ORDER_TITLE, headerFont);
        List<String> cells = new ArrayList<>();
        PdfPTable table;

        addElement(title);
        addSeparator();

        if (vendorName != null) {
            cells.add("Customer name");
            cells.add("Vendor name");
            cells.add(customerName);
            cells.add(vendorName);
            table = cellsToTable(cells,2);
        } else {
            cells.add("Customer name");
            cells.add(customerName);
            table = cellsToTable(cells,2);
        }

        addElement(table);
        addSeparator();
        cells = new ArrayList<>();
        cells.add("P.O number");
        cells.add("Order date");
        cells.add("Status");
        cells.add(poNumber);
        cells.add(poDate);
        cells.add(poStatus);

        table = cellsToTable(cells,3);
        addElement(table);
        addSeparator();
    }

    private void printData(
        Document pdf,
        List<List<String>> itemCollection,
        boolean price,
        String total_amount,
        PurchaseOrderReportDTO pOrder,
        String vendorName,
        boolean allVendors) {

        String poNumber = pOrder.getPurchaseOrderNumber(),
                poDate = pOrder.getPurchaseOrderDate(),
                poStatus = pOrder.getStatus(),
                customerName = pOrder.getUserInformation().getUser().getLastName() + " "
                            + pOrder.getUserInformation().getUser().getFirstName();
        List<String> menu = new ArrayList<>();

        if (price) {
            menu.add("Item number");
            menu.add("Size");
            menu.add("Color");
            menu.add("Quantity");
            menu.add("Price");
            menu.add("Subtotal");

            printDataWithMenu(pdf, itemCollection, menu, poNumber, poDate, poStatus, customerName, vendorName, price);
            printTotal(pdf, total_amount);
            printFooter(pdf, itemCollection.size()+"", itemCollection.size()+"");
        } else {
            menu.add("Item number");
            menu.add("Size");
            menu.add("Color");
            menu.add("Quantity");

            printDataWithMenu(pdf, itemCollection, menu, poNumber, poDate, poStatus, customerName, vendorName, price);
            printFooter(pdf, itemCollection.size()+"", itemCollection.size()+"");
        }
    }

    private void printDataWithMenu(Document pdf,
                                   List<List<String>> itemCollection,
                                   List<String> menu,
                                   String poNumber,
                                   String poDate,
                                   String poStatus,
                                   String customerName,
                                   String vendorName,
                                   boolean price) {
        for(int i = 0; i < itemCollection.size(); i++) {
            List<String> page = itemCollection.get(i);
            page.addAll(0,menu);
            printMenu(pdf,poNumber,poDate,poStatus,customerName,vendorName);
            printPage(pdf, page, price);

            if (i < itemCollection.size() - 1) {
                printFooter(pdf, i + 1 + "", itemCollection.size() + "");
                pdf.newPage();
            }
        }
    }

    private void printPage(Document pdf, List<String> cells, boolean price) {
        if (!cells.isEmpty()) {
            int rowsNumber = price ? ADMIN_REPORT_TABLE_COLUMNS : CUSTOMER_REPORT_TABLE_COLUMNS ;
            addElement(cellsToTable(cells, rowsNumber));
        }
    }

    private void printFooter(Document pdf, String pageNo, String noPages) {
        addSeparator();
        addElement(new Paragraph(pageNo + " of " + noPages, textFont));
    }

    private void printTotal(Document pdf, String total) {
        List<List<String>> customerHeader = new ArrayList<>();
        List<String> cells = new ArrayList<>();
        PdfPTable table;

        cells.add("Total");
        cells.add(total);
        table = cellsToTable(cells,2);
        addElement(table);
    }

    private void print(Document pdf, PurchaseOrderReportDTO pOrder, String vendorName, List<List<String>> itemCollection, boolean price, boolean allVendors) {
        // printData
        printData(pdf,itemCollection,price,pOrder.getTotalPrice().toString(), pOrder, vendorName, allVendors);
    }

    private List<List<String>> generateItems(boolean price, boolean allVendors, int vendor, PurchaseOrderReportDTO pOrder, List<User> vendors) {
        /*
            First List is the row
            Second List is the containing rows
            Third List is the Page
         */
        List<List<String>> itemCollection = new ArrayList<>();
        List<PurchaseOrderDetailsReportDTO> itemList = pOrder.getPurchaseOrderDetailsReportDTOList();
        List<String> tCollection = new ArrayList<>();
        Long itemNumber = 0L;
        List<String> item = null;

        for(int i = 0; i < itemList.size(); i++) {
            PurchaseOrderDetailsReportDTO pod = itemList.get(i);

            if(tCollection.size() == 0) {
                itemNumber = pod.getItem().getId();
            }

            item = vendorEvaluator(price,allVendors,vendor,pod,vendors);

            if(itemNumber != pod.getItem().getId()) {
                tCollection.addAll(blankRow(price));
            }

            tCollection.addAll(item);

            if (tCollection.size() > ROWS_PER_PAGE) {
                itemCollection.add(tCollection);
                tCollection = new ArrayList<>();
            }

            itemNumber = pod.getItem().getId();
        }

//        itemCollection.add(tCollection);

        return itemCollection;
    }

    private List<String> vendorEvaluator(boolean price, boolean allVendors, int vendor, PurchaseOrderDetailsReportDTO pod, List<User> vendors) {
        if (allVendors) {
            if (price) {
                return priceEvaluator(price,pod);
            } else {
                if (vendor == pod.getItem().getItemInformation().getUser().getId()) {
                    return priceEvaluator(price,pod);
                }
            }
        } else {
            if (vendor > 0) {
                if (vendor == pod.getItem().getItemInformation().getUser().getId()) {
                    return priceEvaluator(price,pod);
                }
            } else {
                return priceEvaluator(price,pod);
            }
        }
        return Collections.EMPTY_LIST;
    }

    @Transactional(readOnly = true)
    private List<String> priceEvaluator(boolean price, PurchaseOrderDetailsReportDTO pod) {
        UserInformation userInformation = userInformationRepository.findByUserIsCurrentUser();
        List<String> row = new ArrayList<>();

        row.add(pod.getItem().getId().toString());
        row.add(pod.getItem().getSize().getName());
        row.add(pod.getItem().getColor().getName());
        row.add(pod.getQuantity().toString());

        if (price) {
            row.add((ItemPriceHelper.getPriceWithPriceTier(
                pod.getItem().getPrice(),
                userInformation.getPriceTier()
            )).toString());
            row.add((ItemPriceHelper.getPriceWithPriceTier(
                pod.getItem().getPrice(),
                userInformation.getPriceTier()
            ) * pod.getQuantity()+""));
        }
        return row;
    }

    private List<String> blankRow(boolean price) {
        List<String> row = new ArrayList<>();

        row.add(SCORE_CHAR);
        row.add(EMPTY_CHAR);
        row.add(EMPTY_CHAR);
        row.add(EMPTY_CHAR);

        if (price) {
            row.add(EMPTY_CHAR);
            row.add(EMPTY_CHAR);
        }
        return row;
    }

    public void evaluator(boolean price, boolean allVendors, int vendor, String vendorName,
                          PurchaseOrderReportDTO pOrder, List<User> vendors, FileOutputStream pdfFile) throws DocumentException {
        List<List<String>> itemCollection;
        Document pdf = this.document = new Document();

        PdfWriter.getInstance(pdf, pdfFile);
        open();
        if (allVendors) {
            if (price) {
                itemCollection = generateItems(price, allVendors, vendor, pOrder, vendors);
                print(pdf, pOrder,vendorName,itemCollection, price, allVendors);
            } else {
                for(int i = 0; i < vendors.size(); i++) {
                    User v = vendors.get(i);
                    itemCollection = generateItems(price, allVendors, v.getId().intValue(), pOrder, vendors);
                    print(pdf, pOrder, v.getLastName() + " " + v.getFirstName(), itemCollection, price, allVendors);
                    if (i < vendors.size() - 1) {
                        newPage();
                    }
                }
            }
        } else {
            itemCollection = generateItems(price, allVendors, vendor, pOrder, vendors);
            print(pdf, pOrder, vendorName, itemCollection, price, allVendors);
        }
        close();
    }

    private PdfPTable cellsToTable(List<String> cells, int columnsQuantity) {
        return cellsToTable(cells,columnsQuantity, true);
    }

    private PdfPTable cellsToTable(List<String> cells, int columnsQuantity, boolean hasHeader) {
        PdfPTable table = new PdfPTable(columnsQuantity);
        for(int i = 0; i < cells.size(); i++) {
            PdfPCell cell;
            if (hasHeader && i < columnsQuantity) {
                cell = titleCell(cells.get(i));
            } else {
                cell = textCell(cells.get(i));
            }
            table.addCell(cell);
        }
        return table;
    }

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

    private PdfPCell addItemSeparator() {
        return createCell(textBackground, textFont, EMPTY_CHAR);
    }

    private void addSeparator() {
        Paragraph paragraph = new Paragraph(EMPTY_CHAR, titleFont);
        addElement(paragraph);
    }

    private void newPage() {
        document.newPage();
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

    public Document getDocument() {
        return this.document;
    }

}
