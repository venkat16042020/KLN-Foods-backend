package Food_Orders.Controller;

import Food_Orders.Entity.Payment;
import Food_Orders.Entity.PaymentStatus;

import Food_Orders.Service.PaymentServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentServices paymentServices;


    @PostMapping("/add")
    public ResponseEntity<Payment> addPayment(@Validated @RequestBody Payment payment) {
        Payment createdPayment = paymentServices.addPayment(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentServices.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }


    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getPaymentByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentServices.getPaymentByTransactionId(transactionId);
        if (payment == null) {
            logger.warn("Payment with transaction ID {} not found.", transactionId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Payment with transaction ID " + transactionId + " not found.");
        }
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }


    @PutMapping("/transaction/{transactionId}/status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String transactionId, @Validated @RequestBody PaymentStatus paymentStatus) {
        Payment updatedPayment = paymentServices.updatePaymentStatus(transactionId, paymentStatus.getStatus());
        if (updatedPayment == null) {
            logger.warn("Failed to update payment status for transaction ID {}", transactionId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Payment with transaction ID " + transactionId + " not found.");
        }
        logger.info("Payment status updated successfully for transaction ID {}", transactionId);
        return ResponseEntity.ok(updatedPayment);
    }


    @GetMapping("/status/{transactionId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String transactionId) {
        PaymentStatus paymentStatus = paymentServices.getPaymentStatus(transactionId);
        if (paymentStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Payment status for transaction ID " + transactionId + " not found.");
        }
        return ResponseEntity.ok(paymentStatus);
    }
}
