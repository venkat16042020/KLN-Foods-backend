package Food_Orders.Entity;

import lombok.Getter;

@Getter
public class PaymentStatus {
    private String transactionId;
    private String status;


    public PaymentStatus(String transactionId, String status) {
        this.transactionId = transactionId;
        this.status = status;
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
