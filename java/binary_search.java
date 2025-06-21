import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class binary_search {

    static class Pair {
        long number;
        String text;

        Pair(long number, String text) {
            this.number = number;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        File file = null;

        // Use JFileChooser to select CSV file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV dataset file for binary search");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String fullPath = file.getAbsolutePath();
            System.out.println("File FOUND: " + fullPath);

            List<Pair> dataList = ParseFile(fullPath);
            if (dataList == null) {
                System.out.println("Failed to read or parse the file.");
                return;
            }

            binarySearch(dataList);
        } else {
            System.out.println("No file selected. Exiting.");
        }
    }

    private static List<Pair> ParseFile(String filename) {
        List<Pair> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                long number = Long.parseLong(parts[0].trim());
                String text = parts[1].trim();
                list.add(new Pair(number, text));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + filename);
            return null;
        }
        return list;
    }

    private static int binarySearch(List<Pair> list) {
        StringBuilder output = new StringBuilder();
        int n = list.size();
        final int TIMING_ITERATIONS = 1000;

        // Warmup JVM
        System.out.println("Warming up JVM...");
        for (int i = 0; i < 1000; i++) {
            binarySearchSingle(list, list.get(i % n).number);
        }

        // ---------------------------- BEST CASE -----------------------------
        long bestTarget = list.get(n / 2).number;

        long startTime = System.nanoTime();
        int bestComparisons = 0;
        for (int i = 0; i < TIMING_ITERATIONS; i++) {
            bestComparisons = binarySearchSingle(list, bestTarget);
        }
        long endTime = System.nanoTime();

        double bestTime = (endTime - startTime) / 1_000_000.0 / TIMING_ITERATIONS;
        output.append(String.format("Best Case Time   : %.6f ms\n", bestTime));
        output.append("Best Case Comparisons: " + bestComparisons + "\n\n");

        // ---------------------------- WORST CASE -----------------------------
        List<Long> worstCaseTargets = new ArrayList<>();
        int maxComparisons = 0;
        long worstCaseAnalysisStart = System.nanoTime();

        // Test ALL elements to find true worst case
        for (int i = 0; i < n; i++) {
            if (i % 1000000 == 0 && i > 0) {
                System.out.println("Progress: " + i + "/" + n + " elements tested");
            }

            int comparisons = binarySearchSingle(list, list.get(i).number);
            if (comparisons > maxComparisons) {
                maxComparisons = comparisons;
                worstCaseTargets.clear();
                worstCaseTargets.add(list.get(i).number);
            } else if (comparisons == maxComparisons) {
                worstCaseTargets.add(list.get(i).number);
            }
        }

        // Test non-existent value
        long nonExistentTarget = list.get(n - 1).number + 1;
        int nonExistentComparisons = binarySearchSingle(list, nonExistentTarget);
        if (nonExistentComparisons >= maxComparisons) {
            maxComparisons = nonExistentComparisons;
            worstCaseTargets.add(nonExistentTarget);
        }

        long worstCaseAnalysisEnd = System.currentTimeMillis();
        System.out.println("Worst case analysis completed in " +
                (worstCaseAnalysisEnd - worstCaseAnalysisStart) / 1000.0 + " seconds");
        System.out
                .println("Found " + worstCaseTargets.size() + " elements requiring " + maxComparisons + " comparisons");

        // Time the worst case searches
        startTime = System.nanoTime();
        for (int i = 0; i < TIMING_ITERATIONS; i++) {
            binarySearchSingle(list, worstCaseTargets.get(i % worstCaseTargets.size()));
        }
        endTime = System.nanoTime();

        double worstTime = (endTime - startTime) / 1_000_000.0 / TIMING_ITERATIONS;
        output.append(String.format("Worst Case Time  : %.6f ms\n", worstTime));
        output.append("Worst Case Comparisons: " + maxComparisons + "\n");
        output.append("Elements requiring max comparisons: " + worstCaseTargets.size() + "\n\n");

        // ---------------------------- AVERAGE CASE -----------------------------
        System.out.println("Analyzing Average Case - Testing all " + n + " elements...");
        long totalComparisons = 0;
        long averageCaseStart = System.currentTimeMillis();

        // Test ALL elements for true average
        for (int i = 0; i < n; i++) {
            if (i % 1000000 == 0 && i > 0) {
                System.out.println("Progress: " + i + "/" + n + " elements tested");
            }
            totalComparisons += binarySearchSingle(list, list.get(i).number);
        }

        double trueAverageComparisons = (double) totalComparisons / n;
        long averageCaseEnd = System.currentTimeMillis();
        System.out.println("Average case analysis completed in " +
                (averageCaseEnd - averageCaseStart) / 1000.0 + " seconds");

        // Time a representative sample for average timing
        Random random = new Random(42); // Fixed seed for reproducibility
        List<Long> averageTargets = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            averageTargets.add(list.get(random.nextInt(n)).number);
        }

        startTime = System.nanoTime();
        for (int i = 0; i < TIMING_ITERATIONS; i++) {
            for (long target : averageTargets) {
                binarySearchSingle(list, target);
            }
        }
        endTime = System.nanoTime();

        double averageTime = (endTime - startTime) / 1_000_000.0 / TIMING_ITERATIONS / averageTargets.size();

        output.append(String.format("Average Case Time: %.6f ms\n", averageTime));
        output.append(String.format("True Average Comparisons: %.6f\n", trueAverageComparisons));

        System.out.println("\n" + output.toString());
        writeToFile("java/output/binary_search/binary_search_" + n + ".txt", output.toString());
        return (int) totalComparisons;
    }

    private static int binarySearchSingle(List<Pair> list, long target) {
        int left = 0;
        int right = list.size() - 1;
        int comparisons = 0;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            comparisons++;

            long middleValue = list.get(middle).number;
            if (middleValue == target) {
                return comparisons;
            } else if (middleValue < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return comparisons;
    }

    private static void writeToFile(String path, String content) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
            System.out.println("Output written to: " + path);
        } catch (IOException e) {
            System.out.println("Failed to write output file.");
            e.printStackTrace();
        }
    }
}