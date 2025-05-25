package bookkeeping.system.bookkeepingsys.util;

import bookkeeping.system.bookkeepingsys.model.Budget;
import bookkeeping.system.bookkeepingsys.model.Transaction;
import bookkeeping.system.bookkeepingsys.model.Category;
import bookkeeping.system.bookkeepingsys.model.Note;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.StandardOpenOption;

public class BudgetManager {
    private static final String DATABASE_DIR = "databases";
    private static final String CSV_FILE = DATABASE_DIR + "/budgets.csv";
    private static final String NOTES_FILE = DATABASE_DIR + "/notes.csv";
    private static final String CSV_HEADER = "id,yearMonth,targetAmount,currentAmount";
    private static final String NOTES_HEADER = "id,budgetId,content,timestamp";

    static {
        try {
            Files.createDirectories(Paths.get(DATABASE_DIR));
            if (!Files.exists(Paths.get(CSV_FILE))) {
                Files.write(Paths.get(CSV_FILE), CSV_HEADER.getBytes());
            }
            if (!Files.exists(Paths.get(NOTES_FILE))) {
                Files.write(Paths.get(NOTES_FILE), NOTES_HEADER.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取所有预算记录
    public static List<Budget> getAllBudgets() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        List<Budget> budgets = new ArrayList<>();
        
        if (lines.size() > 1) {
            budgets = lines.stream()
                .skip(1) // 跳过表头
                .filter(line -> !line.trim().isEmpty())
                .map(Budget::fromString)
                .collect(Collectors.toList());
        }
        
        return budgets;
    }

    // 获取指定月份的预算
    public static Optional<Budget> getBudgetForMonth(YearMonth yearMonth) throws IOException {
        return getAllBudgets().stream()
            .filter(budget -> budget.getYearMonth().equals(yearMonth))
            .findFirst();
    }

    // 保存预算
    public static void saveBudget(Budget budget) throws IOException {
        List<Budget> budgets = getAllBudgets();
        
        if (budget.getId() == null || budget.getId().trim().isEmpty()) {
            // 新预算
            budget.setId(UUID.randomUUID().toString());
            budgets.add(budget);
        } else {
            // 更新现有预算
            budgets.removeIf(b -> b.getId().equals(budget.getId()));
            budgets.add(budget);
        }

        // 写入文件
        List<String> lines = new ArrayList<>();
        lines.add(CSV_HEADER);
        lines.addAll(budgets.stream().map(Budget::toString).collect(Collectors.toList()));
        Files.write(Paths.get(CSV_FILE), lines);
    }

    // 删除预算
    public static void deleteBudget(String id) throws IOException {
        List<Budget> budgets = getAllBudgets();
        budgets.removeIf(budget -> budget.getId().equals(id));
        
        List<String> lines = new ArrayList<>();
        lines.add(CSV_HEADER);
        lines.addAll(budgets.stream().map(Budget::toString).collect(Collectors.toList()));
        Files.write(Paths.get(CSV_FILE), lines);
    }

    // 更新当月预算进度
    public static void updateCurrentAmount(YearMonth yearMonth) throws IOException {
        Optional<Budget> budgetOpt = getBudgetForMonth(yearMonth);
        if (!budgetOpt.isPresent()) {
            return;
        }

        Budget budget = budgetOpt.get();
        List<Transaction> transactions = TransactionManager.getAllTransactions();
        
        // 计算当月储蓄金额（收入 - 支出）
        double savings = transactions.stream()
            .filter(t -> YearMonth.from(t.getDateTime()).equals(yearMonth))
            .mapToDouble(t -> {
                if (t.getType() == Category.CategoryType.INCOME) {
                    return t.getAmount();
                } else {
                    return -t.getAmount();
                }
            })
            .sum();

        budget.setCurrentAmount(savings);
        saveBudget(budget);
    }

    // 保存备忘录
    public static void saveNote(Note note) throws IOException {
        List<Note> notes = getAllNotes();
        notes.removeIf(n -> n.getId().equals(note.getId()));
        notes.add(note);
        
        List<String> lines = new ArrayList<>();
        lines.add(NOTES_HEADER);
        lines.addAll(notes.stream().map(Note::toString).collect(Collectors.toList()));
        Files.write(Paths.get(NOTES_FILE), lines);
    }

    // 删除备忘录
    public static void deleteNote(String noteId) throws IOException {
        List<Note> notes = getAllNotes();
        notes.removeIf(note -> note.getId().equals(noteId));
        
        List<String> lines = new ArrayList<>();
        lines.add(NOTES_HEADER);
        lines.addAll(notes.stream().map(Note::toString).collect(Collectors.toList()));
        Files.write(Paths.get(NOTES_FILE), lines);
    }

    // 获取所有备忘录
    private static List<Note> getAllNotes() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(NOTES_FILE));
        List<Note> notes = new ArrayList<>();
        
        if (lines.size() > 1) {
            notes = lines.stream()
                .skip(1) // 跳过表头
                .filter(line -> !line.trim().isEmpty())
                .map(Note::fromString)
                .collect(Collectors.toList());
        }
        
        return notes;
    }

    // 获取特定预算的备忘录
    public static List<Note> getNotesForBudget(String budgetId) throws IOException {
        return getAllNotes().stream()
            .filter(note -> note.getBudgetId().equals(budgetId))
            .sorted()
            .collect(Collectors.toList());
    }
} 