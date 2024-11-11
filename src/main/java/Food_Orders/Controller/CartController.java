package Food_Orders.Controller;

import Food_Orders.Dto.CartItemDto;
import Food_Orders.Dto.OrderRequest;
import Food_Orders.Entity.Address;
import Food_Orders.Entity.Cart;
import Food_Orders.Entity.CartItem;
import Food_Orders.Repository.AddressRepository;
import Food_Orders.Repository.CartItemRepository;
import Food_Orders.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {

            Cart cart = new Cart();
            cart.setTotalAmount(orderRequest.getTotalAmount());
            cart.setPhoneNumber(orderRequest.getPhoneNumber());
            cart.setEmail(orderRequest.getEmail());
            cart.setDate(orderRequest.getDate());
            Cart savedCart = cartRepository.save(cart);

            // Save cart items
            for (CartItemDto itemDto : orderRequest.getItems()) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(savedCart);
                cartItem.setName(itemDto.getName());
                cartItem.setPrice(itemDto.getPrice());
                cartItem.setQuantity(itemDto.getQuantity());
                cartItem.setTotalGST(itemDto.getTotalGST());
                cartItemRepository.save(cartItem);
            }

            // Save address
            Address address = new Address();
            address.setCart(savedCart);
            address.setHouseNumber(orderRequest.getAddress().getHouseNumber());
            address.setLandMark(orderRequest.getAddress().getLandMark());
            address.setStreet(orderRequest.getAddress().getStreet());
            address.setCity(orderRequest.getAddress().getCity());
            address.setState(orderRequest.getAddress().getState());
            address.setZipCode(orderRequest.getAddress().getZipCode());
            addressRepository.save(address);

            // Prepare a JSON response message
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
}
