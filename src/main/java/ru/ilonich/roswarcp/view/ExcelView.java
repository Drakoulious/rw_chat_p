package ru.ilonich.roswarcp.view;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<List<String>> result = (List<List<String>>) model.get("result");
        Sheet sheet = workbook.createSheet("Result");
        sheet.setHorizontallyCenter(true);
        Map<String, CellStyle> styles = createStyles(workbook);
        Row head = sheet.createRow(0);
        List<String> columnNames = result.get(0);
        for (int i = 0; i < columnNames.size(); i++) {
            String value = columnNames.get(i);
            Cell cell = head.createCell(i);
            cell.setCellValue(value);
            cell.setCellStyle(styles.get("header"));
            sheet.setColumnWidth(i, (value.length() + 5)*256);
        }
        for (int i = 1; i < result.size(); i++) {
            List<String> columnValues = result.get(i);
            Row row = sheet.createRow(i);
            for (int j = 0; j < columnValues.size(); j++) {
                String value = columnValues.get(j);
                Cell cell = row.createCell(j);
                cell.setCellValue(value);
                cell.setCellStyle(styles.get("cellright"));
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
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
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
