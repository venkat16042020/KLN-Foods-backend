package Food_Orders.Service;
import Food_Orders.Entity.Food;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class FoodExcelExportUtils {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Food> foodList;

    public FoodExcelExportUtils(List<Food> foodList) {
        this.foodList = foodList;
       workbook = new XSSFWorkbook();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else {
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(style);
    }

    private void createHeaderRow() {
        sheet = workbook.createSheet("foods");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        createCell(row, 0, "Food Items Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Price", style);
        createCell(row, 3, "State GST", style);
        createCell(row, 4, "Central GST", style);
        createCell(row, 5, "Total GST", style);
        createCell(row, 6, "Description", style);
        createCell(row, 7, "Total Price", style);
        createCell(row, 8, "Category Name", style);
        createCell(row, 9, "Image URL", style);

    }

    private void writeFoodData() {
        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);


        for (Food food:foodList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row,columnCount++,food.getId(),style);
            createCell(row,columnCount++,food.getName(),style);
            createCell(row,columnCount++,food.getPrice(),style);
            createCell(row,columnCount++,food.getStateGST(),style);
            createCell(row,columnCount++,food.getCentralGST(),style);
            createCell(row,columnCount++,food.getTotalGST(),style);
            createCell(row,columnCount++,food.getDescription(),style);
            createCell(row,columnCount++,food.getTotalPrice(),style);
            createCell(row,columnCount++,food.getCategoryName(),style);
            createCell(row,columnCount++,food.getImageUrl(),style);
            System.out.println("Writing food to Excel: " + food);

        }
    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeFoodData();
       ServletOutputStream outputStream = response.getOutputStream();
       workbook.write(outputStream);
       workbook.close();
       outputStream.close();
    }
}
