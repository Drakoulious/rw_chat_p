package ru.ilonich.roswarcp.view;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import ru.ilonich.roswarcp.model.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Message> messages = (List<Message>) model.get("messages");
        Sheet sheet = workbook.createSheet("System messages");
        sheet.setDefaultColumnWidth(20);
        Row head = sheet.createRow(0);
        head.createCell(0).setCellValue("ID");
        head.createCell(1).setCellValue("Date");
        head.createCell(2).setCellValue("Player ID");
        head.createCell(3).setCellValue("Nickname");
        head.createCell(4).setCellValue("Level");
        head.createCell(5).setCellValue("Fraction");
        head.createCell(6).setCellValue("Clan name");
        head.createCell(7).setCellValue("Clan ID");
        head.createCell(8).setCellValue("Item name");
        head.createCell(9).setCellValue("Item count");
        head.createCell(10).setCellValue("Text");
        head.createCell(11).setCellValue("Type");
        head.createCell(12).setCellValue("Channel");
        head.createCell(13).setCellValue("Room ID");
        head.createCell(14).setCellValue("Linked player ID");
        int rowCount = 1;
        for (Message m : messages){
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(m.getId());
            row.createCell(1).setCellValue(m.getTime());
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
        }
    }
}
