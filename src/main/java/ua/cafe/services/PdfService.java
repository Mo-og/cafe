package ua.cafe.services;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import ua.cafe.models.Order;
import ua.cafe.utils.Stats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;

@Service
public class PdfService {

    public static final String DEST = ".//src//main//resources//static//reports//";
    public static final String FORMAT = ".pdf";

    private OrderService orderService;

    public static Path getPathFromInterval(Stats.Interval interval, boolean sortByQuantity, boolean includeZeros) {
        return Path.of(DEST, interval.formatForPath() + (sortByQuantity ? "_Q" : "_C") + (includeZeros ? "Z" : "") + FORMAT);
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String getPdf(Stats.Interval interval, boolean sortByQuantity, boolean includeZeros, boolean override) {
        File file = new File(getPathFromInterval(interval, sortByQuantity, includeZeros).toUri());
        if (!override && file.exists()) {
            try {
                return Base64Utils.encodeToString(FileUtils.readFileToByteArray(file));
            } catch (IOException e) {
                throw new RuntimeException("Pdf reading failed: " + e.getMessage());
            }
        }
        Order order = orderService.getReportOrder(interval, sortByQuantity, includeZeros);
        return generatePdf(order, interval, sortByQuantity, includeZeros);
    }

    public String generatePdf(Order order, Stats.Interval interval, boolean sortByQuantity, boolean includeZeros) {
        Path path = getPathFromInterval(interval, sortByQuantity, includeZeros);

        try {
            File file = new File(path.toUri());
            if (!file.exists()) {
                file.createNewFile();
            }

            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(path.toString()));
            doc.open();

            Font introductionFont = new Font(Font.HELVETICA, 14, Font.BOLD, BLACK);
            Font fromToFont = new Font(Font.HELVETICA, 14, Font.BOLDITALIC, GREEN);

            doc.add(new Paragraph("Отчет по продажам", introductionFont));
            doc.add(new Paragraph("с " + Stats.formatForUser(interval.from), fromToFont));
            doc.add(new Paragraph("по " + Stats.formatForUser(interval.to), fromToFont));
            doc.add(new Paragraph("Общая прибыль:  " + order.getCost(), introductionFont));

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
