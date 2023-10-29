import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Logger logger = new Logger();
//        logger.enableDebug();
        logger.Debug("Main is running");

        // Add expenses
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.loadExpensesFromFile("expenses.txt");

        // Start the main menu loop
        showMainMenu(tracker);
    }

    private static void showMainMenu(ExpenseTracker tracker) {
        // Create a Scanner for user input
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Main menu loop
        while (true) {
            System.out.println("Expense Tracker Menu:");
            System.out.println("1. Add an Expense");
            System.out.println("2. Edit an Expense");
            System.out.println("3. Delete an Expense");
            System.out.println("4. View All Expenses");
            System.out.println("5. View Expenses by Month and Year");
            System.out.println("6. Calculate Monthly Total");
            System.out.println("7. Calculate Category Total");
            System.out.println("8. Search Expenses");
            System.out.println("9. Export Expenses to File");
            System.out.println("Q. Quit");
            System.out.print("Select an option: ");

            userInput = scanner.nextLine().trim().toUpperCase();
            switch (userInput) {
                case "1":
                    // Add an expense
                    System.out.print("Enter date (e.g., October 1 2023): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    tracker.addExpense(date, category, description, amount);
                    break;
                case "2":
                    // Edit an expense
                    System.out.print("Enter description to search for expenses to edit: ");
                    String searchKeyword = scanner.nextLine();
                    List<Expense> matchingExpenses = tracker.searchExpenses(searchKeyword);

                    if (!matchingExpenses.isEmpty()) {
                        System.out.print("Enter the index of the expense to edit: ");
                        int indexToEdit = Integer.parseInt(scanner.nextLine());
                        if (indexToEdit >= 0 && indexToEdit < matchingExpenses.size()) {
                            Expense expenseToEdit = matchingExpenses.get(indexToEdit);
                            System.out.print("Enter new date (e.g., October 10 2023): ");
                            String newDate = scanner.nextLine();
                            System.out.print("Enter new category: ");
                            String newCategory = scanner.nextLine();
                            System.out.print("Enter new description: ");
                            String newDescription = scanner.nextLine();
                            System.out.print("Enter new amount: ");
                            double newAmount = Double.parseDouble(scanner.nextLine());
                            tracker.editExpense(expenseToEdit, newDate, newCategory, newDescription, newAmount);
                        } else {
                            System.out.println("Invalid index. No expense edited.");
                        }
                    } else {
                        System.out.println("No matching expenses found.");
                    }
                    break;
                case "3":
                    // Delete an expense
                    System.out.print("Enter description to search for expenses to delete: ");
                    searchKeyword = scanner.nextLine();
                    matchingExpenses = tracker.searchExpenses(searchKeyword);

                    if (!matchingExpenses.isEmpty()) {
                        System.out.print("Enter the index of the expense to delete: ");
                        int indexToDelete = Integer.parseInt(scanner.nextLine());
                        if (indexToDelete >= 0 && indexToDelete < matchingExpenses.size()) {
                            Expense expenseToDelete = matchingExpenses.get(indexToDelete);
                            tracker.deleteExpense(expenseToDelete);
                        } else {
                            System.out.println("Invalid index. No expense deleted.");
                        }
                    } else {
                        System.out.println("No matching expenses found.");
                    }
                    break;
                case "4":
                    // View all expenses
                    tracker.viewExpenses();
                    break;
                case "5":
                    // View expenses by month and year
                    System.out.print("Enter month (e.g., October): ");
                    String month = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    tracker.viewExpensesByMonth(month, year);
                    break;
                case "6":
                    // Calculate monthly total
                    System.out.print("Enter month (e.g., October): ");
                    month = scanner.nextLine();
                    System.out.print("Enter year: ");
                    year = Integer.parseInt(scanner.nextLine());
                    double monthlyTotal = tracker.getMonthlyTotal(month, year);
                    System.out.println("Total expenses for " + month + " " + year + ": $" + monthlyTotal);
                    break;
                case "7":
                    // Calculate category total
                    System.out.print("Enter category: ");
                    category = scanner.nextLine();
                    double categoryTotal = tracker.getCategoryTotal(category);
                    System.out.println("Total expenses in the '" + category + "' category: $" + categoryTotal);
                    break;
                case "8":
                    // Search expenses
                    System.out.print("Enter keyword to search: ");
                    searchKeyword = scanner.nextLine();
                    tracker.searchExpenses(searchKeyword);
                    break;
                case "9":
                    // Export expenses to a file
                    tracker.exportExpensesToFile("expenses.txt");
                    break;
                case "Q":
                    // Quit the program
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
