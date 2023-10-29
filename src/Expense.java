import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Expense
{
    private String date;
    private String category;
    private String description;
    private double amount;

    public Expense(String date, String category, String description, double amount) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy");

    public int getYear() {
        try {
            Date dateObject = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObject);
            return cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            // Handle the parsing exception
            return -1; // Return a sentinel value or throw an exception as needed
        }
    }

    public int getMonth() {
        try {
            Date dateObject = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObject);
            return cal.get(Calendar.MONTH) + 1; // Months are 0-based, so add 1
        } catch (ParseException e) {
            // Handle the parsing exception
            return -1; // Return a sentinel value or throw an exception as needed
        }
    }

    public int getDay() {
        try {
            Date dateObject = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObject);
            return cal.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            // Handle the parsing exception
            return -1; // Return a sentinel value or throw an exception as needed
        }
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String toExportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(date).append("\n");
        sb.append("Category: ").append(category).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Amount: ").append(amount).append("\n");
        sb.append("-----\n");
        return sb.toString();
    }

    public static Expense fromExportString(String exportString) {
        String[] lines = exportString.split("\n");
        String date = null;
        String category = null;
        String description = null;
        double amount = 0.0;

        for (String line : lines) {
            if (line.startsWith("Date: ")) {
                date = line.substring(6).trim();
            } else if (line.startsWith("Category: ")) {
                category = line.substring(10).trim();
            } else if (line.startsWith("Description: ")) {
                description = line.substring(13).trim();
            } else if (line.startsWith("Amount: ")) {
                amount = Double.parseDouble(line.substring(7).trim());
            }
        }

        return new Expense(date, category, description, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return date.equals(expense.date) &&
                description.equals(expense.description) &&  // Add description comparison
                category.equals(expense.category) &&
                Double.compare(expense.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, description, category, amount);  // Include description in hashing
    }
}
