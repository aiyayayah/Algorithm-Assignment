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
        int inputListSize = list.size();
        final int TIMING_ITERATIONS = 1000;

        /// ---------------------------- WARM UP JVM -----------------------------
        System.out.println("Warming up JVM...");
        for (int i = 0; i < 1000; i++) {
            binarySearchSingle(list, list.get(i % inputListSize).number);
        }

        // ---------------------------- BEST CASE -----------------------------
        int left = 0;
        int right = list.size() - 1;
        long bestCaseTarget = list.get((left + right) / 2).number;

        long bestCaseStartTime = System.nanoTime();
        int bestCaseComparison = 0;
        for (int i = 0; i < TIMING_ITERATIONS; i++) {
            bestCaseComparison = binarySearchSingle(list, bestCaseTarget);
        }
        long bestCaseEndTime = System.nanoTime();

        double bestCaseRunTime = (bestCaseEndTime - bestCaseStartTime) / 1_000_000.0 / TIMING_ITERATIONS;
        output.append(String.format("Best Case Time   : %.6f ms\n", bestCaseRunTime));
        // output.append("Best Case Comparisons: " + bestCaseComparison + "\n\n");

        // ---------------------------- WORST CASE -----------------------------
        List<Long> worstCaseTargets = new ArrayList<>();
        int maxComparisons = 0;

        for (int i = 0; i < inputListSize; i++) {
            int comparisons = binarySearchSingle(list, list.get(i).number);
            if (comparisons > maxComparisons) {
                maxComparisons = comparisons;
                worstCaseTargets.clear(); // clear smaller comparison
                worstCaseTargets.add(list.get(i).number); // add the bigger comparison
            } else if (comparisons == maxComparisons) {
                worstCaseTargets.add(list.get(i).number);
            }
        }

        // Value greater than all elements
        long targetGreater = list.get(inputListSize - 1).number + 1;
        int comparisonsGreater = binarySearchSingle(list, targetGreater);
        if (comparisonsGreater >= maxComparisons) {
            if (comparisonsGreater > maxComparisons) {
                maxComparisons = comparisonsGreater;
                worstCaseTargets.clear();
            }
            worstCaseTargets.add(targetGreater);
        }

        // Value smaller than all elements
        long targetSmaller = list.get(0).number - 1;
        int comparisonsSmaller = binarySearchSingle(list, targetSmaller);
        if (comparisonsSmaller >= maxComparisons) {
            if (comparisonsSmaller > maxComparisons) {
                maxComparisons = comparisonsSmaller;
                worstCaseTargets.clear();
            }
            worstCaseTargets.add(targetSmaller);
        }
        /*
         * -----------------------TO DEBUG EACH ELEMENTS-------------------------------
         * for (int i = 0; i < inputListSize; i++) {
         * long target = list.get(i).number;
         * int comparisons = binarySearchSingle(list, target);
         * output.append(String.format("i: %d  comp: %d\n", i, comparisons));
         * 
         * }
         */

        long worstCaseStartTime = System.nanoTime();
        for (int i = 0; i < TIMING_ITERATIONS; i++) {
            binarySearchSingle(list, worstCaseTargets.get(i % worstCaseTargets.size()));
        }
        long worstCaseEndTime = System.nanoTime();

        double worstCaseRunTime = (worstCaseEndTime - worstCaseStartTime) / 1_000_000.0 / TIMING_ITERATIONS;
        output.append(String.format("Worst Case Time  : %.6f ms\n", worstCaseRunTime));
        // output.append("Worst Case Comparisons: " + maxComparisons + "\n");
        // output.append("Elements requiring max comparisons: " +
        // worstCaseTargets.size() + "\n\n");

        // ---------------------------- AVERAGE CASE -----------------------------
        long totalComparisons = 0;
        long totalSearchTime = 0;

        for (int i = 0; i < inputListSize; i++) {
            long target = list.get(i).number;

            long startTime = System.nanoTime();
            int comparisons = binarySearchSingle(list, target);
            long endTime = System.nanoTime();

            totalSearchTime += (endTime - startTime);
            totalComparisons += comparisons;
        }

        double totalTimeMs = totalSearchTime / 1_000_000.0;
        double averageTimeMs = totalTimeMs / inputListSize;
        double averageComparisons = (double) totalComparisons / inputListSize;

        // output.append(String.format("Total Average Case Time: %.6f ms\n",
        // totalTimeMs));
        output.append(String.format("Average Case Time: %.6f ms\n", averageTimeMs));
        // output.append(String.format("Average Comparisons per Search: %.6f\n",
        // averageComparisons));

        String baseDir = System.getProperty("user.dir"); // current working directory
        String outputPath = baseDir + File.separator + "java" + File.separator + "output" +
                File.separator + "binary_search" + File.separator +
                "binary_search_" + inputListSize + ".txt";

        writeToFile(outputPath, output.toString());

        return (int) totalComparisons;
    }

    private static int binarySearchSingle(List<Pair> list, long target) {
        int left = 0;
        int right = list.size() - 1;
        int comparisons = 0;

        while (left <= right) {
            int middle = (left + right) / 2;
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