package com.ims.inventory.helpers;

import com.ims.inventory.domen.request.SaleRequest;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.*;
import java.io.IOException;

@Service
public class InvoicePdfService {

    public void exportInvoice(SaleRequest saleRequest, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLACK);
        Paragraph title = new Paragraph("INVOICE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Transaction Code: " + saleRequest.getTranCode()));
        document.add(new Paragraph("Customer ID: " + saleRequest.getCustomer()));
        document.add(new Paragraph("Date: " + saleRequest.getDate()));
        document.add(new Paragraph("Status: " + saleRequest.getStatus()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setWidths(new int[]{2, 3, 2, 2, 2, 2, 2, 2, 2});

        addTableHeader(table, "Code", "Name", "Cost", "Qty", "Tax", "TaxAmt", "Disc", "DiscAmt", "Subtotal");

        for (SaleRequest.SaleItemDto item : saleRequest.getItems()) {
            table.addCell(item.getProductCode());
            table.addCell(item.getProductName() != null ? item.getProductName() : "-");
            table.addCell(item.getUnitCost().toPlainString());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(item.getTax() != null ? item.getTax().toPlainString() : "0");
            table.addCell(item.getTaxAmt() != null ? item.getTaxAmt().toPlainString() : "0");
            table.addCell(item.getDiscount() != null ? item.getDiscount().toPlainString() : "0");
            table.addCell(item.getDiscountAmt() != null ? item.getDiscountAmt().toPlainString() : "0");
            table.addCell(item.getSubTotal().toPlainString());
        }

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Order Tax: " + saleRequest.getOrderTax()));
        document.add(new Paragraph("Discount: " + saleRequest.getDiscount()));
        document.add(new Paragraph("Shipping: " + saleRequest.getShippingCost()));
        document.add(new Paragraph("Grand Total: " + saleRequest.getGrandTotal()));

        document.close();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }

}
