package Food_Orders.Service;

import Food_Orders.Entity.ExcelCategory;
import Food_Orders.Repository.ExcelCategoryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelUploadService {

    public static List<ExcelCategory> getCategoryDataFromExcel(InputStream inputStream) throws IOException {
        List<ExcelCategory> categories = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheet("Categories");
            if (sheet != null) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Skip header
                    ExcelCategory category = new ExcelCategory();
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        Cell cell = cells.next();
                        int columnIndex = cell.getColumnIndex();
                        switch (columnIndex) {
                            case 1 -> category.setName(cell.getStringCellValue());
                            case 2 -> category.setDescription(cell.getStringCellValue());
                            case 3 -> category.setImageUrl(cell.getStringCellValue());
                        }
                    }
                    categories.add(category);
                }
            }
        }
        return categories;
    }
}
