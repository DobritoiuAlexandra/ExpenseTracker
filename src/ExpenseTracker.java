import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseTracker {
    private final List<Expense> expenses;
    private Logger logger = new Logger();

    public ExpenseTracker() {
        this.expenses = new ArrayList<>();

    }

    // Add an expense
    public void addExpense(String date, String category, String description, double amount) {
        Expense newExpense = new Expense(date, category, description, amount);

        // Check if an expense with the same date and description already exists
        boolean isDuplicate = expenses.stream()
                .anyMatch(expense -> expense.getDate().equals(date) && expense.getDescription().equals(description));

        if (isDuplicate) {
            // Handle the duplicate expense (e.g., update it or show an error message)
            logger.Info("Duplicate expense found. You can choose to update it or take appropriate action.");
        } else {
            expenses.add(newExpense);
            logger.Info("Expense added: Date: " + date + ", Category: " + category + ", Description: " + description + ", Amount: " + amount);
        }
    }

    // Edit an existing expense
    public void editExpense(Expense expense, String newDate, String newCategory, String newDescription, double newAmount) {
        String oldDate = expense.getDate();
        String oldCategory = expense.getCategory();
        String oldDescription = expense.getDescription();
        double oldAmount = expense.getAmount();

        expense = new Expense(newDate, newCategory, newDescription, newAmount);
        logger.Info("Expense edited: Date: " + oldDate + " to " + newDate + ", Category: " + oldCategory + " to " + newCategory +
                ", Description: " + oldDescription + " to " + newDescription + ", Amount: " + oldAmount + " to " + newAmount);
    }

    // Delete an expense
    public void deleteExpense(Expense expense) {
        String deletedDate = expense.getDate();
        String deletedCategory = expense.getCategory();
        String deletedDescription = expense.getDescription();
        double deletedAmount = expense.getAmount();

        expenses.remove(expense);
        logger.Debug("Expense deleted: Date: " + deletedDate + ", Category: " + deletedCategory + ", Description: " + deletedDescription + ", Amount: " + deletedAmount);
    }

    // Calculate the monthly total for a specific month and year
    public double getMonthlyTotal(String month, int year) {
        double total = 0;
        for (Expense expense : expenses) {
            if (expense.getDate().startsWith(month + " " + year)) {
                total += expense.getAmount();
            }
        }
        logger.Debug("Calculated monthly total for " + month + " " + year + ": " + total);
        return total;
    }

    // Calculate the total expenses for a specific category
    public double getCategoryTotal(String category) {
        double total = 0;
        for (Expense expense : expenses) {
            if (expense.getCategory().equals(category)) {
                total += expense.getAmount();
            }
        }
        logger.Debug("Calculated total expenses for category '" + category + "': " + total);
        return total;
    }

    // View all expenses
    public void viewExpenses() {
        for (Expense expense : expenses) {
            logger.Info("Viewing expense: Date: " + expense.getDate() + ", Category: " + expense.getCategory() + ", Description: " + expense.getDescription() + ", Amount: " + expense.getAmount());
            logger.Info("----------------------------------------------------------------");
        }
    }

    // View expenses for a specific month and year
    public void viewExpensesByMonth(String month, int year) {
        for (Expense expense : expenses) {
            if (expense.getDate().startsWith(month + " " + year)) {
                logger.Info("Viewing expense: Date: " + expense.getDate() + ", Category: " + expense.getCategory() + ", Description: " + expense.getDescription() + ", Amount: " + expense.getAmount());
                logger.Info("----------------------------------------------------------------");
            }
        }
    }

    public void loadExpensesFromFile(String filename) {
        Set<Expense> existingExpenses = new HashSet<>(expenses);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder expenseData = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                expenseData.append(line).append("\n");
                // Check if we've reached the end of an expense entry
                if (line.equals("-----")) {
                    String expenseString = expenseData.toString().trim();
                    Expense loadedExpense = Expense.fromExportString(expenseString);

                    // Check if an expense with the same date and description already exists
                    if (!existingExpenses.contains(loadedExpense)) {
                        expenses.add(loadedExpense);
                        existingExpenses.add(loadedExpense);
                    }

                    // Reset expenseData for the next entry
                    expenseData.setLength(0);
                }
            }

            logger.Debug("Expenses loaded from " + filename);
        } catch (IOException e) {
            logger.Error("Error loading expenses from file: " + e.getMessage());
        }
    }

    public void exportExpensesToFile(String filename) {
        sortExpensesByDateAndAmount();
        viewExpenses();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Expense expense : expenses) {
                writer.write(expense.toExportString());
            }
            logger.Debug("Expenses exported to " + filename);
        } catch (IOException e) {
            logger.Error("Error exporting expenses to file: " + e.getMessage());
        }
    }

    // Search for expenses by keywords in the description or category
    public List<Expense> searchExpenses(String keyword) {
        List<Expense> matchingExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getCategory().toLowerCase().contains(keyword.toLowerCase()) ||
                    expense.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingExpenses.add(expense);
                logger.Info("Found matching expense: Date: " + expense.getDate() + ", Category: " + expense.getCategory() + ", Description: " + expense.getDescription() + ", Amount: " + expense.getAmount());
            }
        }
        return matchingExpenses;
    }

    public void sortExpensesByDateAndAmount() {
        expenses.sort((e1, e2) -> {
            int yearComparison = e1.getYear() - e2.getYear();
            if (yearComparison != 0) {
                return yearComparison;
            }

            int monthComparison = e1.getMonth() - e2.getMonth();
            if (monthComparison != 0) {
                return monthComparison;
            }

            int dayComparison = e1.getDay() - e2.getDay();
            if (dayComparison != 0) {
                return dayComparison;
            }

            return Double.compare(e1.getAmount(), e2.getAmount());
        });
        logger.Debug("Expenses sorted by date and amount.");
    }

}
