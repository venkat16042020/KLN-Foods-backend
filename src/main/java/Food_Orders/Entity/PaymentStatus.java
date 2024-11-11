package Food_Orders.Entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentStatus {
    private String transactionId;
    private String status;


    public PaymentStatus(String transactionId, String status) {
        this.transactionId = transactionId;
        this.status = status;
    }


}
