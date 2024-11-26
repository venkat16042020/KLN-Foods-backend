package Food_Orders.Service;

import Food_Orders.Entity.Food;
import Food_Orders.Repository.FoodRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public void saveFoodsToDatabase(MultipartFile file) {
        try {
            if (!FoodExcelUploadService.isValidExcelFile(file)) {
                throw new IllegalArgumentException("Invalid file format. Only Excel files are allowed.");
            }
            List<Food> foods = FoodExcelUploadService.getFoodsDataFromExcel(file.getInputStream());
            foodRepository.saveAll(foods);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving foods to the database: " + e.getMessage());
        }
    }

    public void exportFoodsToExcel(HttpServletResponse response) throws IOException {
        System.out.println("Starting exportFoodsToExcel");
        List<Food> foods = foodRepository.findAll();
        System.out.println("Foods data to be exported: " + foods);
        new FoodExcelExportUtils(foods).exportDataToExcel(response);
        System.out.println("Foods data successfully exported to Excel");
    }
}
