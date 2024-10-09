package Food_Orders.Service;

import Food_Orders.Entity.Payment;
import Food_Orders.Entity.PaymentStatus;
import Food_Orders.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServices {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentByTransactionId(String transactionId) {
        // Use the custom query method defined in the PaymentRepository
        return paymentRepository.findByTransactionId(transactionId);
    }

    public Payment updatePaymentStatus(String transactionId, String status) {
        Payment payment = getPaymentByTransactionId(transactionId);
        if (payment != null) {
            payment.setStatus(status);
            return paymentRepository.save(payment);
        }
        return null;
    }

    public PaymentStatus getPaymentStatus(String transactionId) {
        Payment payment = getPaymentByTransactionId(transactionId);
        if (payment != null) {
            return new PaymentStatus(payment.getTransactionId(), payment.getStatus());
        }
        return null;
    }
}
