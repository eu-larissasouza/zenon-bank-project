package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
        int step,
        Type type,
        BigDecimal amount,
        Customer custOrig,
        Customer custDest,
        boolean isFraud,
        boolean isFlaggedFraud
) {
    
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
