package bookkeeping.system.bookkeepingsys.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note implements Comparable<Note> {
    private String id;
    private String budgetId;
    private String content;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Note(String id, String budgetId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.budgetId = budgetId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // 用于从CSV创建Note对象
    public static Note fromString(String line) {
        String[] parts = line.split(",", 4);
        return new Note(
            parts[0],
            parts[1],
            parts[2],
            LocalDateTime.parse(parts[3], FORMATTER)  // 使用FORMATTER解析时间字符串
        );
    }

    // 用于保存到CSV
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s",
            id, budgetId, content, timestamp.format(FORMATTER));
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }

    @Override
    public int compareTo(Note other) {
        return other.timestamp.compareTo(this.timestamp); // 按时间倒序排列
    }
} 