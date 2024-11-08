package Food_Orders.Service;

import Food_Orders.Entity.Category;
import Food_Orders.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {
            if (categoryRepository.count() == 0) {  // Check if categories table is empty
                List<Category> defaultCategories = Arrays.asList(
                        new Category("Snack", "Delicious snacks", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwrbtbNQssrsoi2TYu4UHF0KNhmZJOy1uWVw2pZLRqE80YXZpbPKwo48EIAyfA-xq7XVo&usqp=CAU"),
                        new Category("Grains", "Healthy grains", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfYm2nrBsW5VngnjXg9cRZi6tOkP8vLjIjYA&s"),
                        new Category("Dairy", "Fresh dairy products", "https://sp-ao.shortpixel.ai/client/to_webp,q_lossless,ret_img/https://blog.sathguru.com/wp-content/uploads/2021/10/How-modern-dairy-products-are-replenishing-the-dairy-market-in-India-1-1130x580.jpg")
                );

                categoryRepository.saveAll(defaultCategories);
                System.out.println("Default categories added to the database.");
            }
        };
    }
}
