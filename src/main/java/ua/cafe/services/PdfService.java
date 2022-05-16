package ua.cafe.services;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import ua.cafe.models.Order;
import ua.cafe.utils.Stats;

import java.io.File;
import java.io.FileOutputStream;

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;

@Service
public class PdfService {

    public static final String DEST = "hello.pdf";

    public String generatePdf(Order order, Stats.Interval interval) {
        try {
            File file = new File(DEST);
            if (!file.exists()) {
                file.createNewFile();
            }

            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST));
            doc.open();

            Font introductionFont = new Font(Font.HELVETICA, 14, Font.BOLD, BLACK);
            Font fromToFont = new Font(Font.HELVETICA, 14, Font.BOLDITALIC, GREEN);

            Paragraph introParagraph = new Paragraph("Отчет по продажам", introductionFont);
            Paragraph fromDateParagraph = new Paragraph("с " + Stats.formatForUser(interval.from), fromToFont);
            Paragraph toDateParagraph = new Paragraph("с " + Stats.formatForUser(interval.to), fromToFont);
            Paragraph costParagraph = new Paragraph("Общая прибыль:  " + order.getCost(), introductionFont);

            PdfPTable table = new PdfPTable(3);

            table.setWidthPercentage(100);

            PdfPCell cell = new PdfPCell();
            // table headers
            cell.setPhrase(new Phrase("Блюдо", introductionFont));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Продано порций", introductionFont));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Прибыль (грн.)", introductionFont));
            table.addCell(cell);

            order.acquireDetails().forEach(detail -> {
                table.addCell(detail.getDish().getName());
                table.addCell(String.valueOf(detail.getQuantity()));
                table.addCell(String.valueOf(detail.getCost()));
            });

            table.setSpacingBefore(15f);
            table.setSpacingAfter(15f);

            doc.add(introParagraph);
            doc.add(fromDateParagraph);
            doc.add(toDateParagraph);
            doc.add(costParagraph);

            doc.add(table);

            doc.close();
            writer.flush();
            writer.close();
            return Base64Utils.encodeToString(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            throw new RuntimeException("Pdf generation failed: " + e.getMessage());
        }
    }
}
