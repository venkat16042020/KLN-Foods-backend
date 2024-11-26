package Food_Orders.Controller;

import Food_Orders.Dto.CartItemDto;
import Food_Orders.Dto.OrderRequest;
import Food_Orders.Entity.Address;
import Food_Orders.Entity.Cart;
import Food_Orders.Entity.CartItem;
import Food_Orders.Repository.AddressRepository;
import Food_Orders.Repository.CartItemRepository;
import Food_Orders.Repository.CartRepository;
import Food_Orders.Service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // Create a new Cart
            Cart cart = new Cart();
            cart.setTotalAmount(orderRequest.getTotalAmount());
            cart.setPhoneNumber(orderRequest.getPhoneNumber());
            cart.setEmail(orderRequest.getEmail());
            cart.setDate(orderRequest.getDate());

            double sumAllGST = 0.0; // Accumulator for GST
            double totalPrice = 0.0; // Accumulator for total price

            // Save cart items and calculate sumAllGST and totalPrice
            List<CartItem> cartItems = new ArrayList<>();
            for (CartItemDto itemDto : orderRequest.getItems()) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setName(itemDto.getName());
                cartItem.setPrice(itemDto.getPrice());
                cartItem.setQuantity(itemDto.getQuantity());
                cartItem.setTotalGST(itemDto.getTotalGST());

                // Add to sumAllGST and totalPrice
                sumAllGST += itemDto.getTotalGST() * itemDto.getQuantity(); // Adjust to account for quantity
                totalPrice += itemDto.getPrice() * itemDto.getQuantity();

                cartItems.add(cartItem);
            }

            // Set sumAllGST and totalPrice in the Cart entity
            cart.setCartItems(cartItems);
            cart.setSumAllGST(sumAllGST);
            cart.setTotalPrice(totalPrice);

            // Save the Cart
            Cart savedCart = cartRepository.save(cart);

            // Save the address
            Address address = new Address();
            address.setCart(savedCart);
            address.setHouseNumber(orderRequest.getAddress().getHouseNumber());
            address.setLandMark(orderRequest.getAddress().getLandMark());
            address.setStreet(orderRequest.getAddress().getStreet());
            address.setCity(orderRequest.getAddress().getCity());
            address.setState(orderRequest.getAddress().getState());
            address.setZipCode(orderRequest.getAddress().getZipCode());
            addressRepository.save(address);

            // Prepare a success response
            Map<String, String> response = new HashMap<>();
            response.put("message", "Order placed successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to place order.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    @GetMapping("/orders")
    public ResponseEntity<List<Cart>> getAllOrders() {
        List<Cart> orders = cartRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Cart> getOrderById(@PathVariable Long id) {
        Optional<Cart> order = cartRepository.findById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/byEmail")
    public ResponseEntity<List<Cart>> getOrdersByEmail(@RequestParam String email) {
        try {
            List<Cart> orders = cartRepository.findByEmail(email);
            if (orders.isEmpty()) {
                // Instead of 404, return 200 OK with an empty list
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Cart> updateOrder(@PathVariable Long id, @RequestBody Cart updatedCart) {
        return cartRepository.findById(id)
                .map(cart -> {
                    cart.setTotalAmount(updatedCart.getTotalAmount());
                    cart.setPhoneNumber(updatedCart.getPhoneNumber());
                    cart.setEmail(updatedCart.getEmail());
                    cart.setDate(updatedCart.getDate());
                    Cart savedCart = cartRepository.save(cart);
                    return ResponseEntity.ok(savedCart);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
        return cartRepository.findById(id)
                .map(cart -> {
                    cartRepository.delete(cart);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/summary")
    public ResponseEntity<Map<String, Object>> getOrderSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", cartRepository.count());
        summary.put("totalSales", cartRepository.findAll().stream()
                .mapToDouble(Cart::getTotalAmount)
                .sum());
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/import")
    public ResponseEntity<String> uploadCartData(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Received request to upload cart file: " + file.getOriginalFilename());
            cartService.saveCartsToDatabase(file);  // Call the CartService to save data
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cart data uploaded and saved to database successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading cart file: " + e.getMessage());
        }
    }

    // Endpoint to export cart data to an Excel file
    @GetMapping("/Export")
    public void exportCartDataToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Carts_Information.xlsx";
        response.setHeader(headerKey, headerValue);

        cartService.exportCartsToExcel(response);
        System.out.println("Cart data exported successfully.");
    }
}
