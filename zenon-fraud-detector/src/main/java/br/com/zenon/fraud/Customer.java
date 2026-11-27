package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Customer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
    public Customer {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(EResponses.NAME_REQUIRED.getMessage());
        }
        if (oldBalance == null) {
            throw new IllegalArgumentException(EResponses.OLD_BALANCE_REQUIRED.getMessage());
        }
        if (oldBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(EResponses.OLD_BALANCE_INVALID.withValue(oldBalance));
        }
        if (newBalance == null) {
            throw new IllegalArgumentException(EResponses.NEW_BALANCE_REQUIRED.getMessage());
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(EResponses.NEW_BALANCE_INVALID.withValue(newBalance));
        }
    }
}
