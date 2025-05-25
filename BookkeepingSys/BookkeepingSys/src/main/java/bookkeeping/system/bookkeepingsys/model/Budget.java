package bookkeeping.system.bookkeepingsys.model;

import java.time.YearMonth;

public class Budget {
    private String id;
    private YearMonth yearMonth;
    private double targetAmount;
    private double currentAmount;

    public Budget(String id, YearMonth yearMonth, double targetAmount, double currentAmount) {
        this.id = id;
        this.yearMonth = yearMonth;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    // 用于从CSV创建预算对象
    public static Budget fromString(String line) {
        String[] parts = line.split(",");
        return new Budget(
            parts[0],
            YearMonth.parse(parts[1]),
            Double.parseDouble(parts[2]),
            Double.parseDouble(parts[3])
        );
    }

    // 用于保存到CSV
    @Override
    public String toString() {
        return String.format("%s,%s,%.2f,%.2f",
            id, yearMonth, targetAmount, currentAmount);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    // 计算进度百分比
    public double getProgress() {
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }
} 