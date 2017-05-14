package ru.ilonich.roswarcp.view;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import ru.ilonich.roswarcp.model.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Message> messages = (List<Message>) model.get("messages");
        Sheet sheet = workbook.createSheet("System messages");
        sheet.setHorizontallyCenter(true);
        Map<String, CellStyle> styles = createStyles(workbook);
        Row head = sheet.createRow(0);
        head.createCell(0).setCellValue("ID");
        sheet.setColumnWidth(0, 11*256);
        head.createCell(1).setCellValue("Date");
        sheet.setColumnWidth(1, 19*256);
        head.createCell(2).setCellValue("Player ID");
        sheet.setColumnWidth(2, 10*256);
        head.createCell(3).setCellValue("Nickname");
        sheet.setColumnWidth(3, 16*256);
        head.createCell(4).setCellValue("Level");
        sheet.setColumnWidth(4, 7*256);
        head.createCell(5).setCellValue("Fraction");
        sheet.setColumnWidth(5, 13*256);
        head.createCell(6).setCellValue("Clan name");
        sheet.setColumnWidth(6, 19*256);
        head.createCell(7).setCellValue("Clan ID");
        sheet.setColumnWidth(7, 8*256);
        head.createCell(8).setCellValue("Item name");
        sheet.setColumnWidth(8, 30*256);
        head.createCell(9).setCellValue("Item count");
        sheet.setColumnWidth(9, 9*256);
        head.createCell(10).setCellValue("Text");
        sheet.setColumnWidth(10, 73*256);
        head.createCell(11).setCellValue("Type");
        sheet.setColumnWidth(11, 7*256);
        head.createCell(12).setCellValue("Channel");
        sheet.setColumnWidth(12, 7*256);
        head.createCell(13).setCellValue("Room ID");
        sheet.setColumnWidth(13, 7*256);
        head.createCell(14).setCellValue("Linked player ID");
        sheet.setColumnWidth(14, 10*256);
        for (int i = 0; i <= 14; i++) {
            head.getCell(i).setCellStyle(styles.get("header"));
        }
        int rowCount = 1;
        for (Message m : messages){
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(m.getId());
            row.createCell(1).setCellValue(formatter.format(m.getTime()));
            row.createCell(2).setCellValue(m.getPlayerId());
            row.createCell(3).setCellValue(m.getNickName());
            row.createCell(4).setCellValue(m.getLevel());
            row.createCell(5).setCellValue(m.getFraction());
            row.createCell(6).setCellValue(m.getClanName());
            row.createCell(7).setCellValue(m.getClanId());
            row.createCell(8).setCellValue(m.getItemName());
            row.createCell(9).setCellValue(m.getItemCount());
            row.createCell(10).setCellValue(m.getText());
            row.createCell(11).setCellValue(m.getType());
            row.createCell(12).setCellValue(m.getChannel());
            row.createCell(13).setCellValue(m.getRoomId());
            row.createCell(14).setCellValue(m.getLinkedPlayerId());
            for (int i = 0; i <= 14; i++) {
                if (i == 1 || i == 3) {
                    row.getCell(i).setCellStyle(styles.get("cellcenter"));
                } else {
                    row.getCell(i).setCellStyle(styles.get("cellright"));
                }
            }
        }
    }

    private Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;

        Font propFont = wb.createFont();
        propFont.setFontHeightInPoints((short)12);
        propFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(propFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellcenter", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellright", style);

        return styles;
    }

}
