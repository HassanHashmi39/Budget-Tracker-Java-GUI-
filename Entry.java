package BudgetTracker;

import java.time.LocalDate;

public class Entry {
    private String type;
    private String source;
    private double amount;
    private LocalDate date;

    public Entry(String type, String source, double amount, LocalDate date) {
        this.type = type;
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    public String getType() { return type; }
    public String getSource() { return source; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return type + " | " + source + " | " + amount + " | " + date;
    }
}
