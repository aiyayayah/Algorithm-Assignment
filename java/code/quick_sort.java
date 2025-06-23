import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class quick_sort {

    public static void main(String[] args) {
        File selectedFile = null;

        // Use JFileChooser to select a CSV file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV dataset file to sort");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        } else {
            System.out.println("No file selected. Exiting.");
            return;
        }

        String inputFilename = selectedFile.getAbsolutePath();
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

        // Quick sort and measure running time
        long startTime = System.currentTimeMillis();
        quickSort(arr, 0, n - 1);
        long endTime = System.currentTimeMillis();

        // Write sorted integer-string pairs to output file
        // Prepare dynamic output path using project root
        String baseDir = System.getProperty("user.dir");
        String folderPath = baseDir + File.separator + "java" + File.separator + "output" + File.separator
                + "quick_sort";
        File outputDir = new File(folderPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String outputFilename = folderPath + File.separator + "quick_sort_" + n + ".csv";

        // Write sorted integer-string pairs to output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            for (Pair p : arr) {
                writer.write(p.value + "," + p.str);
                writer.newLine();
            }
            System.out.println("Sorted dataset written to " + outputFilename);
        } catch (IOException e) {
            System.err.println("Error writing file: " + outputFilename);
            e.printStackTrace();
        }

        double runningTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Quick sort running time: " + runningTimeSeconds + " seconds");
    }

    private static void quickSort(Pair[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

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

    private static class Pair {
        int value;
        String str;

        Pair(int value, String str) {
            this.value = value;
            this.str = str;
        }
    }
}
