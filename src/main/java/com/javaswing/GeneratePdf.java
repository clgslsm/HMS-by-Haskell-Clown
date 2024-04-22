package com.javaswing;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.javaswing.ExportMedicinePanel.generateBillID;
import static java.awt.Component.CENTER_ALIGNMENT;

public class GeneratePdf {
    public static void main(String [] args) throws FileNotFoundException {
        String path= STR."C://bill//invoice.pdf";
        Rectangle one = new Rectangle(180,600);
        Document doc = new Document(one);
        doc.setMargins(10, 10, 30, 30);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();

            // Add title to the document
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
            Paragraph title = new Paragraph("ABC HOSPITAL", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);

            // Add a horizontal line
            // Add dashed line
            // Add dashes as separator
            com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 7);
            String dashes = " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
            Paragraph lineSeparator = new Paragraph(dashes,font);
            lineSeparator.setAlignment(Element.ALIGN_CENTER);
            doc.add(lineSeparator);

            // Add Bill ID and date
            String billID = generateBillID();
            String currentDate = getCurrentDate();
            Paragraph billInfo = new Paragraph("Bill ID: " + billID + "\nDate: " + currentDate,font);
            billInfo.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(billInfo);
            doc.add(lineSeparator);

            // Add table
            PdfPTable tb1 = new PdfPTable(2);
            tb1.setWidthPercentage(95);
            tb1.getDefaultCell().setBackgroundColor(null);
            tb1.getDefaultCell().setPadding(5);
            tb1.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tb1.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            // Add cells to table
            PdfPCell cell1 = new PdfPCell(new Phrase("Medicine", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)));
            PdfPCell cell2 = new PdfPCell(new Phrase("No of Units", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)));

            // Set alignment for cells
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tb1.addCell(cell1);
            tb1.addCell(cell2);

            doc.add(new Paragraph("\n"));
            doc.add(tb1);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph(""));

            // Add content to the document
            doc.add(lineSeparator);


            // Close the document
            doc.close();
            System.out.println(STR."PDF created successfully at: \{path}");
        }
        catch (Exception e){

        }
    }
    private static String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return currentDateTime.format(formatter);
    }
}
