//package Food_Orders.Controller;
//
//import Food_Orders.Entity.Payment;
//import Food_Orders.Repository.PaymentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentController {
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @PostMapping("/process")
//    public ResponseEntity<Map<String, String>> processPayment(@RequestBody Payment payment) {
//        Map<String, String> response = new HashMap<>();
//
//        if (payment.getTotalAmount() > 0) {
//            payment.setPaymentStatus(1); // Success
//            payment.setOrderStatus("Success");
//            paymentRepository.save(payment);
//
//            response.put("status", "success");
//            response.put("message", "Payment Successful");
//            return ResponseEntity.ok(response);
//        } else {
//            payment.setPaymentStatus(0); // Failed
//            payment.setOrderStatus("Failed");
//            paymentRepository.save(payment);
//
//            response.put("status", "failed");
//            response.put("message", "Payment Failed");
//            return ResponseEntity.status(400).body(response);
//        }
//    }
//}
