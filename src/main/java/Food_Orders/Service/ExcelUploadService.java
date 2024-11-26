package Food_Orders.Service;

import Food_Orders.Entity.Category;
import org.apache.poi.ss.usermodel.Cell;
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

public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
    public static List<Category> getCategoriesDataFromExcel(InputStream inputStream) {
        List<Category> categories = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Categories");
            int rowIndex = 0;
            for (Row row : sheet){
                if (rowIndex == 0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Category category = new Category();
                while (cellIterator.hasNext()){
                    Cell cell  = cellIterator.next();
                    switch (cellIndex) {
                        case 0 -> category.setCategory_id((long) cell.getNumericCellValue());
                        case 1 -> category.setName(cell.getStringCellValue());
                        case 2 -> category.setDescription(cell.getStringCellValue());
                        case 3 -> category.setImageUrl(cell.getStringCellValue());
                        default -> {

                        }
                    }
                    cellIndex++;
                }
                categories.add(category);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return categories;
    }
}
