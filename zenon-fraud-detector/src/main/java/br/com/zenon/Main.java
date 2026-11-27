package br.com.zenon;


import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.io.TransactionIngestor;

import java.io.IOException;
import java.util.List;

public class Main {
    
    public static void main ( String[] args ) throws IOException {
        String fileName = "paysim_with_bad_data.csv";

        TransactionIngestor ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.ingest(fileName);

        System.out.println(transactions.size());
        transactions.forEach(System.out::println);
    }
}