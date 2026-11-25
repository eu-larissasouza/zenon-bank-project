package br.com.zenon;


import br.com.zenon.fraud.Customer;
import br.com.zenon.fraud.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    
    public static void main ( String[] args ) {
        Transaction tx1 = new Transaction(1, Transaction.Type.PAYMENT, new BigDecimal("9839.64"), new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")), new Customer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")), false, false);
        
        Transaction tx2 = new Transaction(743, Transaction.Type.CASH_OUT, new BigDecimal("850002.52"), new Customer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")), new Customer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")), true, false);
        
        List<Transaction> transactions = List.of(tx1, tx2);
        transactions.forEach(System.out::println);
    }
}