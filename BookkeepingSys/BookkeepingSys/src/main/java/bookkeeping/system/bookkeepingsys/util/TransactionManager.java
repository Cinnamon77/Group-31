package bookkeeping.system.bookkeepingsys.util;

import bookkeeping.system.bookkeepingsys.model.Transaction;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionManager {
    private static final String DATABASE_DIR = "databases";
    private static final String CSV_FILE = DATABASE_DIR + "/transactions.csv";
    private static final String CSV_HEADER = "id,date,amount,categoryId,categoryName,description,type";

    static {
        try {
            Files.createDirectories(Paths.get(DATABASE_DIR));
            if (!Files.exists(Paths.get(CSV_FILE))) {
                Files.write(Paths.get(CSV_FILE), CSV_HEADER.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getAllTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return transactions;
        }

        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        if (lines.size() <= 1) {  // 如果文件为空或只有表头
            return transactions;
        }

        for (int i = 1; i < lines.size(); i++) { // Skip header
            String line = lines.get(i);
            if (line != null && !line.trim().isEmpty()) {
                try {
                    transactions.add(Transaction.fromString(line));
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line);
                    e.printStackTrace();
                }
            }
        }
        return transactions;
    }

    public static List<Transaction> getRecentTransactions(LocalDateTime since) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return transactions;
        }

        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        if (lines.size() <= 1) {  // 如果文件为空或只有表头
            return transactions;
        }

        for (int i = 1; i < lines.size(); i++) { // Skip header
            String line = lines.get(i);
            if (line != null && !line.trim().isEmpty()) {
                try {
                    Transaction transaction = Transaction.fromString(line);
                    if (transaction.getDateTime().isAfter(since)) {
                        transactions.add(transaction);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line);
                    e.printStackTrace();
                }
            }
        }
        return transactions;
    }

    public static void saveTransaction(Transaction transaction) throws IOException {
        List<String> lines = new ArrayList<>();
        if (Files.exists(Paths.get(CSV_FILE))) {
            lines = Files.readAllLines(Paths.get(CSV_FILE));
        } else {
            lines.add(CSV_HEADER);
        }

        if (transaction.getId() == null || transaction.getId().trim().isEmpty()) {
            transaction = new Transaction(
                UUID.randomUUID().toString(),
                transaction.getDateTime(),
                transaction.getAmount(),
                transaction.getCategoryId(),
                transaction.getCategoryName(),
                transaction.getDescription(),
                transaction.getType()
            );
        }

        boolean updated = false;
        for (int i = 1; i < lines.size(); i++) {
            Transaction existingTransaction = Transaction.fromString(lines.get(i));
            if (existingTransaction.getId().equals(transaction.getId())) {
                lines.set(i, transaction.toString());
                updated = true;
                break;
            }
        }

        if (!updated) {
            lines.add(transaction.toString());
        }

        Files.write(Paths.get(CSV_FILE), lines);
    }

    public static void deleteTransaction(String id) throws IOException {
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        if (lines.isEmpty()) {
            return;
        }

        List<String> newLines = new ArrayList<>();
        newLines.add(lines.get(0)); // Add header

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line != null && !line.trim().isEmpty()) {
                try {
                    Transaction transaction = Transaction.fromString(line);
                    if (!transaction.getId().equals(id)) {
                        newLines.add(line);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line);
                    continue;
                }
            }
        }

        Files.write(Paths.get(CSV_FILE), newLines);
    }
} 