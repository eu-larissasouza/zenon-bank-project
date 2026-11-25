package br.com.zenon.fraud.io;

import br.com.zenon.fraud.Customer;
import br.com.zenon.fraud.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {
    
    private static final int BUFFER_SIZE = 8192; // 8 KB
    private static final int MAX_ROWS = 1000;
    private static final byte NEW_LINE = (byte) '\n';
    private static final byte CARRIAGE_RETURN = (byte) '\r';
    
    public List<Transaction> ingest ( String fileName ) throws IOException {
        Path path = resolvePath(fileName);
        List<Transaction> transactions = new ArrayList<>(MAX_ROWS);
        
        // Acumula os bytes de uma linha caso ela ultrapasse o tamanho do ByteBuffer
        ByteArrayOutputStream currentLineBytes = new ByteArrayOutputStream(256);
        boolean isFirstLine = true;
        int parsedRows = 0;
        
        try ( FileChannel channel = FileChannel.open(path, StandardOpenOption.READ) ) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            
            // Lê blocos de até 8 KB do arquivo até chegar ao EOF (-1) ou atingir o limite de linhas
            while ( parsedRows < MAX_ROWS && channel.read(buffer) != -1 ) {
                // Alterna de modo de escrita (channel -> buffer) para modo de leitura (buffer -> loop)
                buffer.flip();
                
                while ( buffer.hasRemaining() && parsedRows < MAX_ROWS ) {
                    byte b = buffer.get();
                    
                    if ( b == NEW_LINE ) {
                        String line = currentLineBytes.toString(StandardCharsets.UTF_8).trim();
                        currentLineBytes.reset(); // Limpa o acumulador para a próxima linha
                        
                        if ( isFirstLine ) {
                            // Pula o cabeçalho do CSV ("step,type,amount,...")
                            isFirstLine = false;
                            continue;
                        }
                        
                        if ( !line.isEmpty() ) {
                            transactions.add(toTransaction(line));
                            parsedRows++;
                        }
                    } else if ( b != CARRIAGE_RETURN ) {
                        // Ignora '\r' (comum em arquivos com quebra CRLF) e guarda os demais bytes
                        currentLineBytes.write(b);
                    }
                }
                
                // Compacta em vez de apenas limpar: move bytes não lidos para o início e prepara para escrita
                buffer.compact();
            }
            
            // Caso o arquivo termine sem '\n' na última linha e ainda não tenha atingido MAX_ROWS
            if ( parsedRows < MAX_ROWS && currentLineBytes.size() > 0 ) {
                String line = currentLineBytes.toString(StandardCharsets.UTF_8).trim();
                if ( !isFirstLine && !line.isEmpty() ) {
                    transactions.add(toTransaction(line));
                }
            }
        }
        
        return transactions;
    }
    
    private Path resolvePath(String fileName) {
        Path direct = Path.of(fileName);
        if (direct.isAbsolute() || Files.exists(direct)) {
            return direct;
        }
        // Procura na pasta irmã ../data (quando o projeto está em /repositorio/projeto-java)
        return Path.of("..", "data").resolve(fileName).normalize();
    }
    
    private Transaction toTransaction ( String line ) {
        // -1 mantém campos vazios no final da linha, se existirem
        String[] cols = line.split(",", -1);
        
        Integer step = Integer.valueOf(cols[0].trim());
        Transaction.Type type = Transaction.Type.valueOf(cols[1].trim().toUpperCase());
        BigDecimal amount = new BigDecimal(cols[2].trim());
        
        Customer custOrig = new Customer(cols[3].trim(), new BigDecimal(cols[4].trim()), new BigDecimal(cols[5].trim()));
        
        Customer custDest = new Customer(cols[6].trim(), new BigDecimal(cols[7].trim()), new BigDecimal(cols[8].trim()));
        
        Boolean isFraud = Transaction.parseFlag(cols[9]);
        Boolean isFlaggedFraud = Transaction.parseFlag(cols[10]);
        
        return new Transaction(step, type, amount, custOrig, custDest, isFraud, isFlaggedFraud);
    }
}