package br.com.zenon.fraud;

public enum EResponses {
    STEP_INVALID("step should be positive: "),
    TYPE_REQUIRED("type must not be null"),
    AMOUNT_INVALID("amount should be positive: "),
    AMOUNT_REQUIRED("amount must not be null"),
    ORIGIN_REQUIRED("origin customer must not be null"),
    RECIPIENT_REQUIRED("recipient customer must not be null"),
    NAME_REQUIRED("name should not be empty"),
    OLD_BALANCE_REQUIRED("oldBalance must not be null"),
    OLD_BALANCE_INVALID("oldBalance should be positive: "),
    NEW_BALANCE_REQUIRED("newBalance must not be null"),
    NEW_BALANCE_INVALID("newBalance should be positive: ");

    private final String message;

    EResponses(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String withValue(Object value) {
        return message + value;
    }
}