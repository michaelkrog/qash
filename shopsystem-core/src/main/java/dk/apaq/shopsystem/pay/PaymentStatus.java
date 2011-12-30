package dk.apaq.shopsystem.pay;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author krog
 */
public class PaymentStatus {
    
    private final PaymentStatus paymentStatus;
    private final List<HistoryEntry> history;
    private final String orderNumber;
    private final int amount;
    private final String currency;
    private final String gatewayStatus;
    private final String merchantId;
    private final String merchantEmail;
    private final String transactionId;
    private final String cardType;
    private final String cardNumber;
    private final String cardExpire;
    private final boolean splitPayment;
    
    public class HistoryEntry {
        private String type;
        private int amount;
        private PaymentStatus status;
        private Date timestamp;
        private String gatewayStatus;

        public HistoryEntry(String type, int amount, PaymentStatus status, Date timestamp, String gatewayStatus) {
            this.type = type;
            this.amount = amount;
            this.status = status;
            this.timestamp = timestamp;
            this.gatewayStatus = gatewayStatus;
        }

        public int getAmount() {
            return amount;
        }

        public String getGatewayStatus() {
            return gatewayStatus;
        }

        public PaymentStatus getStatus() {
            return status;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public String getType() {
            return type;
        }
    }

    public PaymentStatus(PaymentStatus paymentStatus, List<HistoryEntry> history, String orderNumber, int amount, String currency, 
            String gatewayStatus, String merchantId, String merchantEmail, String transactionId, String cardType, String cardNumber, 
            String cardExpire, boolean splitPayment) {
        this.paymentStatus = paymentStatus;
        this.history = history;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.currency = currency;
        this.gatewayStatus = gatewayStatus;
        this.merchantId = merchantId;
        this.merchantEmail = merchantEmail;
        this.transactionId = transactionId;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardExpire = cardExpire;
        this.splitPayment = splitPayment;
    }

    public int getAmount() {
        return amount;
    }

    public String getCardExpire() {
        return cardExpire;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCurrency() {
        return currency;
    }

    public String getGatewayStatus() {
        return gatewayStatus;
    }

    public List<HistoryEntry> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public boolean isSplitPayment() {
        return splitPayment;
    }

    public String getTransactionId() {
        return transactionId;
    }
    
    
    
}
