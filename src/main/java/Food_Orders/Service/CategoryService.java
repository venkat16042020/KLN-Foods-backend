package Food_Orders.Service;

import Food_Orders.Entity.Category;
import Food_Orders.Repository.CategoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public void saveCategoriesToDatabase(MultipartFile multipartFile) {
        if (ExcelUploadService.isValidExcelFile(multipartFile)) {
            try {
                List<Category> categories = ExcelUploadService.getCategoriesDataFromExcel(multipartFile.getInputStream());
                this.categoryRepository.saveAll(categories);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading Excel file", e);
            } catch (Exception e) {
                throw new IllegalStateException("An error occurred while saving categories", e);
            }
        } else {
            throw new IllegalArgumentException("The uploaded file is not a valid Excel file");
        }
    }

    public List<Category> exportCategoriesToExcel(HttpServletResponse response) throws IOException {
        List<Category> categories = categoryRepository.findAll();
        ExcelExportUtils excelExportUtils = new ExcelExportUtils(categories);
        excelExportUtils.exportDataToexcel(response);
        return categories;
    }
}
