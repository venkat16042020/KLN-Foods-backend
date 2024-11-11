package Food_Orders.Service;



import Food_Orders.Entity.ExcelCategory;
import Food_Orders.Repository.ExcelCategoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelCategoryService {

    private final ExcelCategoryRepository excelCategoryRepository;
    private final ExcelUploadService excelUploadService;

    // Method to import categories from Excel
    public void importCategoriesFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // Get category data from the uploaded Excel file
            List<ExcelCategory> categories = ExcelUploadService.getCategoryDataFromExcel(inputStream);

            // Save the categories into the database
            excelCategoryRepository.saveAll(categories);
        }
    }

    // Method to export categories to Excel
    public void exportCategoriesToExcel(HttpServletResponse response) throws IOException {
        // Retrieve all categories from the database
        List<ExcelCategory> categories = excelCategoryRepository.findAll();

        // Use the utility class to handle the Excel export logic
        ExcelExportUtils exportUtils = new ExcelExportUtils(categories);
        exportUtils.exportDataToExcel(response);  // Write the Excel file to the response output stream
    }
}
