import java.io.*;
import java.util.*;

public class quick_sort_step {

    private static BufferedWriter stepWriter;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Use JFileChooser to select a CSV file
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Select a CSV dataset file to sort");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection != javax.swing.JFileChooser.APPROVE_OPTION) {
                System.out.println("No file selected. Exiting.");
                return;
            }

            String inputFilename = fileChooser.getSelectedFile().getAbsolutePath();
            List<Pair> elements = new ArrayList<>();

            // Read integer-string pairs from dataset file
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        try {
                            int value = Integer.parseInt(parts[0].trim());
                            String str = parts[1].trim();
                            elements.add(new Pair(value, str));
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid integer in line: " + line);
                        }
                    } else {
                        System.out.println("Skipping invalid line (missing string): " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + inputFilename);
                e.printStackTrace();
                return;
            }

            int n = elements.size();
            if (n == 0) {
                System.out.println("No valid integer-string pairs found in the dataset.");
                return;
            }

            Pair[] arr = new Pair[n];
            for (int i = 0; i < n; i++) {
                arr[i] = elements.get(i);
            }

            int startRow = -1;
            int endRow = -1;
            while (startRow < 0 || startRow >= n) {
                System.out.print("Enter start row (0 to " + (n - 1) + "): ");
                if (scanner.hasNextInt()) {
                    startRow = scanner.nextInt();
                    if (startRow < 0 || startRow >= n) {
                        System.out.println("Invalid start row. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }
            while (endRow < startRow || endRow >= n) {
                System.out.print("Enter end row (" + startRow + " to " + (n - 1) + "): ");
                if (scanner.hasNextInt()) {
                    endRow = scanner.nextInt();
                    if (endRow < startRow || endRow >= n) {
                        System.out.println("Invalid end row. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            System.out.println("Starting quicksort from row " + startRow + " to " + endRow + "...");

            String baseDir = System.getProperty("user.dir");
            String folderPath = baseDir + File.separator + "java" + File.separator + "output" + File.separator
                    + "quick_sort_step";

            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String stepFilename = folderPath + File.separator + "quick_sort_step_" + startRow + "_" + endRow + ".txt";

            try {
                stepWriter = new BufferedWriter(new FileWriter(stepFilename));
                quickSort(arr, startRow, endRow);
                stepWriter.close();
                System.out.println("Sorting steps written to " + stepFilename);
            } catch (IOException e) {
                System.err.println("Error writing sorting steps to file: " + stepFilename);
                e.printStackTrace();
            }

        }
    }

    private static void quickSort(Pair[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            printArray(arr, low, high, pi);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(Pair[] arr, int low, int high) {
        int pivot = arr[high].value;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].value <= pivot) {
                i++;
                Pair temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Pair temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    private static void printArray(Pair[] arr, int low, int high, int pivotIndex) {
        try {
            stepWriter.write("Current array state between indexes " + low + " and " + high + ":\n");
            for (int i = low; i <= high; i++) {
                if (i == pivotIndex) {
                    stepWriter.write("-> (" + arr[i].value + ", " + arr[i].str + ") [pivot]\n");
                } else {
                    stepWriter.write("   (" + arr[i].value + ", " + arr[i].str + ")\n");
                }
            }
            stepWriter.write("\n");
            stepWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing sorting steps to file.");
            e.printStackTrace();
        }
    }

    private static class Pair {
        int value;
        String str;

        Pair(int value, String str) {
            this.value = value;
            this.str = str;
        }
    }
}
