package Food_Orders.Controller;


import Food_Orders.Entity.Restaurant;
import Food_Orders.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Get all restaurants
    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantRepository.findAll());
    }

    // Get a restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new restaurant
    @PostMapping("/create")
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("openingHours") String openingHours,
            @RequestParam("imageUrl") String imageUrl) {
        try {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setDescription(description);
            restaurant.setPhoneNumber(phoneNumber); // Set the phone number
            restaurant.setOpeningHours(openingHours); // Set the opening hours
            restaurant.setImageUrl(imageUrl); // Set the image URL

            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            return ResponseEntity.ok(savedRestaurant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // Update a restaurant
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updatedRestaurant) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(updatedRestaurant.getName());
                    restaurant.setDescription(updatedRestaurant.getDescription());
                    restaurant.setPhoneNumber(updatedRestaurant.getPhoneNumber());
                    restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
                    restaurant.setImageUrl(updatedRestaurant.getImageUrl());
                    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
                    return ResponseEntity.ok(savedRestaurant);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a restaurant
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRestaurant(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurantRepository.delete(restaurant);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
