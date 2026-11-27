package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
        int step,
        Type type,
        BigDecimal amount,
        Customer origin,
        Customer recipient,
        boolean isFraud,
        boolean isFlaggedFraud
) {
    public Transaction {
        if (step < 1) {
            throw new IllegalArgumentException(EResponses.STEP_INVALID.withValue(step));
        }
        if (type == null) {
            throw new IllegalArgumentException(EResponses.TYPE_REQUIRED.getMessage());
        }
        if (amount == null) {
            throw new IllegalArgumentException(EResponses.AMOUNT_REQUIRED.getMessage());
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(EResponses.AMOUNT_INVALID.withValue(amount));
        }
        if (origin == null) {
            throw new IllegalArgumentException(EResponses.ORIGIN_REQUIRED.getMessage());
        }
        if (recipient == null) {
            throw new IllegalArgumentException(EResponses.RECIPIENT_REQUIRED.getMessage());
        }
    }
    
    public enum Type {
        CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER
    }
    
    public static Boolean parseFlag(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        return "1".equals(rawValue.trim());
    }
}
