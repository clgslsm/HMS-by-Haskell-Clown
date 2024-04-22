package com.javaswing;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.javaswing.ExportMedicinePanel.generateBillID;
import static java.awt.Component.CENTER_ALIGNMENT;

public class GeneratePdf {
    public static void main(String [] args) throws FileNotFoundException {
        String path= STR."C://bill//invoice.pdf";
        Document doc = new Document(PageSize.HALFLETTER);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();

            // Add title to the document
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Paragraph title = new Paragraph("ABC HOSPITAL", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);

            // Add a horizontal line
            // Add dashed line
            // Add dashes as separator
            String dashes = " - - - - - - - - - - - - - - - - - - - - - - - - - \n";
            Paragraph lineSeparator = new Paragraph(dashes,FontFactory.getFont(FontFactory.COURIER,8));
            lineSeparator.setAlignment(Element.ALIGN_CENTER);
            doc.add(lineSeparator);

            // Add Bill ID and date
            String billID = generateBillID();
            String currentDate = getCurrentDate();
            Paragraph billInfo = new Paragraph("Bill ID: " + billID + "\nDate: " + currentDate,FontFactory.getFont(FontFactory.COURIER,8));
            billInfo.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(billInfo);
            doc.add(lineSeparator);

            // Add table
            PdfPTable tb1 = new PdfPTable(2);
            tb1.addCell("Medicine");
            tb1.addCell("No of Units");

            // Add content to the document
            doc.add(new Paragraph("This is a test PDF document for the bill."));

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
