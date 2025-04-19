package com.ims.inventory.helpers;

import com.ims.inventory.domen.request.SaleRequest;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class InvoicePdf2Service {

    public void exportInvoice(SaleRequest saleRequest, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(40, 40, 80));
            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.DARK_GRAY);
            Font valueFont = new Font(Font.HELVETICA, 10);
            Font tableValueFont = new Font(Font.HELVETICA, 8);
            Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.blue);

            // Title
            Paragraph title = new Paragraph("SALE INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Customer and Sale Info
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 2});

            infoTable.addCell(createCell1("Transaction Code:", labelFont));
            infoTable.addCell(createCell1(saleRequest.getTranCode(), valueFont));

            infoTable.addCell(createCell1("Customer:", labelFont));
            infoTable.addCell(createCell1(saleRequest.getCustomer(), valueFont));

            infoTable.addCell(createCell1("Date:", labelFont));
            infoTable.addCell(createCell1(saleRequest.getDate(), valueFont));

            infoTable.addCell(createCell1("Status:", labelFont) );
            infoTable.addCell(createCell1(saleRequest.getStatus(), valueFont));

            infoTable.addCell(createCell1("Note:", labelFont));
            infoTable.addCell(createCell1(saleRequest.getNote(), valueFont));

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // Sale Items Table
            PdfPTable itemTable = new PdfPTable(9);
            itemTable.setWidthPercentage(100);
            itemTable.setWidths(new float[]{1.5f, 3f, 2f, 1f, 1.5f, 2f, 1.5f, 2f, 3f});

            String[] headers = {"Code", "Name", "Cost", "Qty", "Tax", "TaxAmt", "Disc", "DiscAmt", "Subtotal"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, labelFont));
                headerCell.setBackgroundColor(Color.LIGHT_GRAY);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemTable.addCell(headerCell);
            }

            for (SaleRequest.SaleItemDto item : saleRequest.getItems()) {
                itemTable.addCell(createCell1(item.getProductCode(), tableValueFont));
                itemTable.addCell(createCell1(item.getProductName(), tableValueFont));
                itemTable.addCell(createCell(item.getUnitCost() != null ? item.getUnitCost().toString() : "", tableValueFont));
                itemTable.addCell(createCell(item.getQuantity() != null ? item.getQuantity().toString() : "", tableValueFont));
                itemTable.addCell(createCell(item.getTax() != null ? item.getTax().toString() : "", tableValueFont));
                itemTable.addCell(createCell(item.getTaxAmt() != null ? item.getTaxAmt().toString() : "", tableValueFont));
                itemTable.addCell(createCell(item.getDiscount() != null ? item.getDiscount().toString() : "", tableValueFont));
                itemTable.addCell(createCell(item.getDiscountAmt() != null ? item.getDiscountAmt().toString() : "", tableValueFont));
                itemTable.addCell(createCell(item.getSubTotal() != null ? item.getSubTotal().toString() : "", tableValueFont));
            }

            document.add(itemTable);
            document.add(Chunk.NEWLINE);

            // Totals
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(45);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{1, 1});

            totalTable.addCell(createCell("Order Tax:", labelFont));
            totalTable.addCell(createCell(saleRequest.getOrderTax() != null ? saleRequest.getOrderTax().toString() : "0", valueFont));

            totalTable.addCell(createCell("Discount:", labelFont));
            totalTable.addCell(createCell(saleRequest.getDiscount() != null ? saleRequest.getDiscount().toString() : "0", valueFont));

            totalTable.addCell(createCell("Shipping Cost:", labelFont));
            totalTable.addCell(createCell(saleRequest.getShippingCost() != null ? saleRequest.getShippingCost().toString() : "0", valueFont));

            totalTable.addCell(createCell("Grand Total:", labelFont));
            totalTable.addCell(createCell(saleRequest.getGrandTotal() != null ? saleRequest.getGrandTotal().toString() : "0", totalFont));

            document.add(totalTable);

            // 🔲 Add rectangle border
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(
                    document.leftMargin() - 10,
                    document.bottomMargin() - 10,
                    document.getPageSize().getWidth() - document.rightMargin() + 10,
                    document.getPageSize().getHeight() - document.topMargin() + 10
            );
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1.5f);
            rect.setBorderColor(Color.BLACK);
            canvas.rectangle(rect);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell createCell1(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderColor(Color.GRAY);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColor(Color.GRAY);
        cell.setPadding(5);
        return cell;
    }

}
