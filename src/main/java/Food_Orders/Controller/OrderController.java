package Food_Orders.Controller;

import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// other imports

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    // Add Razorpay Client
    private RazorpayClient razorpayClient;

    @Autowired
    public OrderController() throws Exception {
        this.razorpayClient = new RazorpayClient("rzp_test_HGl3PTqZYOKXbN", "RvOs7McAun7utYyXD9MfMMsk");
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> orderDetails) {
        try {
            // Create an order with Razorpay
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", orderDetails.get("totalAmount")); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1); // Auto-capture payment

            Order order = razorpayClient.orders.create(orderRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to create order."));
        }
    }
}

