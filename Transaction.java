package bookkeeping.system.bookkeepingsys.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String id;
    private LocalDateTime dateTime;
    private double amount;
    private String categoryId;
    private String categoryName;  // 用于显示
    private String description;
    private Category.CategoryType type;  // 支出或收入

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Transaction(String id, LocalDateTime dateTime, double amount, String categoryId, 
                      String categoryName, String description, Category.CategoryType type) {
        this.id = id;
        this.dateTime = dateTime;
        this.amount = amount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.type = type;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category.CategoryType getType() {
        return type;
    }

    public void setType(Category.CategoryType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.join(",",
            id,
            dateTime.format(DATE_TIME_FORMATTER),
            String.valueOf(amount),
            categoryId,
            categoryName.replace(",", "，"),  // 替换英文逗号为中文逗号，避免CSV解析问题
            description.replace(",", "，"),    // 替换英文逗号为中文逗号，避免CSV解析问题
            type.name()
        );
    }

    public static Transaction fromString(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid transaction data: line is null or empty");
        }

        String[] parts = line.split(",");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid transaction data: expected 7 fields, got " + parts.length);
        }

        try {
            return new Transaction(
                parts[0].trim(),  // id
                LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER),  // dateTime
                Double.parseDouble(parts[2].trim()),  // amount
                parts[3].trim(),  // categoryId
                parts[4].trim(),  // categoryName
                parts[5].trim(),  // description
                Category.CategoryType.valueOf(parts[6].trim())  // type
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing transaction data: " + e.getMessage(), e);
        }
    }
} 