package BudgetTracker;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class BudgetTracker extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private java.awt.List list;

    private List<Entry> entries = new ArrayList<>();
    /**
     * @wbp.nonvisual location=548,157
     */
    private final JScrollPane scrollPane = new JScrollPane();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BudgetTracker frame = new BudgetTracker();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BudgetTracker() {
    	setTitle("Buget Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(135, 206, 235));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Source/Category");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(55, 11, 112, 14);
        contentPane.add(lblNewLabel);

        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblAmount.setBounds(55, 36, 58, 14);
        contentPane.add(lblAmount);

        JLabel lblDateddmmyyyy = new JLabel("Date (dd-MM-yyyy)");
        lblDateddmmyyyy.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblDateddmmyyyy.setBounds(55, 72, 130, 14);
        contentPane.add(lblDateddmmyyyy);

        textField = new JTextField();
        textField.setBounds(176, 8, 112, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(176, 33, 112, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setBounds(177, 64, 112, 20);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        JButton btnAddIncome = new JButton("Add Income");
        btnAddIncome.addActionListener(e -> addEntry("Income"));
        btnAddIncome.setForeground(Color.WHITE);
        btnAddIncome.setBackground(new Color(0, 128, 0));
        btnAddIncome.setBounds(32, 116, 120, 23);
        contentPane.add(btnAddIncome);

        JButton btnAddExpense = new JButton("Add Expense");
        btnAddExpense.addActionListener(e -> addEntry("Expense"));
        btnAddExpense.setBackground(new Color(50, 205, 50));
        btnAddExpense.setBounds(158, 116, 112, 23);
        contentPane.add(btnAddExpense);

        JButton btnViewBalance = new JButton("View Balance");
        btnViewBalance.addActionListener(e -> viewBalance());
        btnViewBalance.setBackground(Color.YELLOW);
        btnViewBalance.setBounds(278, 116, 126, 23);
        contentPane.add(btnViewBalance);

        JButton btnViewReports = new JButton("View Reports");
        btnViewReports.addActionListener(e -> viewReports());
        btnViewReports.setBounds(32, 150, 120, 23);
        contentPane.add(btnViewReports);

        JButton btnSortEntries = new JButton("Sort Entries");
        btnSortEntries.addActionListener(e -> sortEntries());
        btnSortEntries.setBounds(158, 150, 112, 23);
        contentPane.add(btnSortEntries);

        JButton btnResetData = new JButton("Reset Data");
        btnResetData.addActionListener(e -> resetData());
        btnResetData.setForeground(Color.WHITE);
        btnResetData.setBackground(Color.RED);
        btnResetData.setBounds(278, 150, 126, 23);
        contentPane.add(btnResetData);

        list = new java.awt.List();
        list.setBounds(40, 179, 400, 100);
        contentPane.add(list);
    }
    
    
//function//
    private void addEntry(String type) {
        String source = textField.getText().trim();
        String amountStr = textField_1.getText().trim();
        String dateStr = textField_2.getText().trim();

        if (source.isEmpty() || amountStr.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateStr, formatter);

            Entry entry = new Entry(type, source, amount, date);
            entries.add(entry);
            list.add(entry.toString());

            textField.setText("");
            textField_1.setText("");
            textField_2.setText("");

            JOptionPane.showMessageDialog(this, type + " added successfully.");

        } 
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in dd-MM-yyyy format.");
        }
    }

    private void viewBalance() {
        double income = entries.stream().filter(e -> e.getType().equals("Income")).mapToDouble(Entry::getAmount).sum();
        double expense = entries.stream().filter(e -> e.getType().equals("Expense")).mapToDouble(Entry::getAmount).sum();
        double balance = income - expense;

        JOptionPane.showMessageDialog(this, "Total Income: " + income + "\nTotal Expense: " + expense + "\nBalance: " + balance);
    }

    private void viewReports() {
        if (entries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No entries to report.");
            return;
        }

        Map<String, Double> categoryTotals = new HashMap<>();

        for (Entry e : entries) {
            if (e.getType().equals("Expense")) {
                categoryTotals.put(e.getSource(), categoryTotals.getOrDefault(e.getSource(), 0.0) + e.getAmount());
            }
        }

        StringBuilder report = new StringBuilder("Category-wise Expenses:\n");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        JOptionPane.showMessageDialog(this, report.toString());
    }

    private void sortEntries() {
        entries.sort(Comparator.comparing(Entry::getDate));
        list.removeAll();
        for (Entry e : entries) {
            list.add(e.toString());
        }
        JOptionPane.showMessageDialog(this, "Entries sorted by date.");
    }

    private void resetData() {
        entries.clear();
        list.removeAll();
        JOptionPane.showMessageDialog(this, "All data has been reset.");
    }
}
