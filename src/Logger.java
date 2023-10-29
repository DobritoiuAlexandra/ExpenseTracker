import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Logger {
    public static final String INFO = "INFO";
    public static final String ERROR = "ERROR";
    public static final String WARNING = "WARNING";
    public static final String DEBUG = "DEBUG";

    private boolean debugEnabled;

    // Create a map to store class names and their corresponding colors
    private static final Map<String, String> classColors = new HashMap<>();
    private static final String[] availableColors = {
            "\u001B[35m", "\u001B[36m", "\u001B[91m", "\u001B[92m", "\u001B[93m",
            "\u001B[94m", "\u001B[95m", "\u001B[96m"
    };

    public Logger() {
        this.debugEnabled = false; // Debug is disabled by default
    }

    public void enableDebug() {
        debugEnabled = true;
    }

    public void disableDebug() {
        debugEnabled = false;
    }

    public void log(String level, String message) {
        if (level.equals(DEBUG) && !debugEnabled) {
            return; // Skip DEBUG messages if debug is not enabled
        }

        String colorCode = getColorCode(level);

        // Get the method name and class name where the log method was called
        String methodName = new Throwable().getStackTrace()[2].getMethodName();
        String className = new Throwable().getStackTrace()[2].getClassName();

        // Assign a random color for the class (if not already assigned)
        classColors.putIfAbsent(className, getRandomColor());

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String classColor = classColors.get(className);
        String classMethodPart = classColor + "[" + className + "::" + methodName + "]" + "\u001B[0m"; // Reset color
        String logMessage = timeStamp + " " + classMethodPart + " " + colorCode + level + "\u001B[0m" + ": " + message;


        String resetColor = "\u001B[0m"; // Reset color to default

        System.out.println(colorCode + logMessage + resetColor);
    }

    public void Info(String message) {
        log(INFO, message);
    }

    public void Error(String message) {
        log(ERROR, message);
    }

    public void Warning(String message) {
        log(WARNING, message);
    }

    public void Debug(String message) {
        if (debugEnabled) {
            log(DEBUG, message);
        }
    }

    // Define color codes for different log levels
    private String getColorCode(String level) {
        switch (level) {
            case INFO:
                return "\u001B[32m"; // Green for INFO
            case ERROR:
                return "\u001B[31m"; // Red for ERROR
            case WARNING:
                return "\u001B[33m"; // Yellow for WARNING
            case DEBUG:
                return "\u001B[34m"; // Blue for DEBUG
            default:
                return "\u001B[0m"; // Default color for other levels
        }
    }

    private String getRandomColor() {
        // Check if all available colors are in use
        if (classColors.size() >= availableColors.length) {
            return "\u001B[35m"; // Purple as the default color
        }

        String color;
        do {
            color = availableColors[new Random().nextInt(availableColors.length)];
        } while (classColors.containsValue(color)); // Ensure the color is not already in use

        return color;
    }
}
