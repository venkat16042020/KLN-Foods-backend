package Food_Orders.Service;

import Food_Orders.Entity.Food;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoodExcelUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FoodExcelUploadService.class);

    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Food> getFoodsDataFromExcel(InputStream inputStream) {
        List<Food> foods = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheet("foods");
            if (sheet == null) {
                logger.error("Sheet named 'foods' not found.");
                return foods;
            }
            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (rowIndex < 2) {
                    rowIndex++;
                    continue;
                }

                Food food = new Food();
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cellIndex) {
                        case 0:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                food.setId((long) cell.getNumericCellValue());
                            }
                            break;
                        case 1:
                            if (cell.getCellType() == CellType.STRING) {
                                food.setName(cell.getStringCellValue());
                            }
                            break;
                        case 2:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                food.setPrice(cell.getNumericCellValue());
                            }
                            break;
                        case 3:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                food.setStateGST(cell.getNumericCellValue());
                            }
                            break;
                        case 4:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                food.setCentralGST(cell.getNumericCellValue());
                            }
                            break;
                        case 5:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                food.setTotalGST(cell.getNumericCellValue());
                            }
                            break;
                        case 6:
                            if (cell.getCellType() == CellType.STRING) {
                                food.setDescription(cell.getStringCellValue());
                            }
                            break;
                        case 7:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                food.setTotalPrice(cell.getNumericCellValue());
                            }
                            break;
                        case 8:
                            if (cell.getCellType() == CellType.STRING) {
                                food.setCategoryName(cell.getStringCellValue());
                            }
                            break;
                        case 9:
                            if (cell.getCellType() == CellType.STRING) {
                                food.setImageUrl(cell.getStringCellValue());
                            }
                            break;
                    }
                    cellIndex++;
                }

                foods.add(food);
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: ", e);
        }

        return foods;
    }

}
