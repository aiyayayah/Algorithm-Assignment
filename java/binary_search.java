import java.io.*;
import java.util.*;

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
        System.out.println("Current working directory: " + new File("").getAbsolutePath());

        String fullPath = GetUserInput();
        File file = new File(fullPath);
        if (!file.exists()) {
            System.out.println("File NOT FOUND: " + fullPath);
            return;
        }

        System.out.println("File FOUND: " + fullPath);

        // Parse the file
        List<Pair> dataList = ParseFile(fullPath);

        if (dataList == null) {
            System.out.println("Failed to read or parse the file.");
            return;
        }
        binarySearch(dataList);
    }

    private static String GetUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the full file path name to perform binary search:");
        return scannerObject.nextLine();
    }

    private static boolean SearchFileName(String targetFileName) {
        File datasetFolder = new File("../output/merge_sort/");

        if (!datasetFolder.exists() || !datasetFolder.isDirectory()) {
            System.out.println("Output folder not found!");
            System.out.println("Please run dataSetGenerator.java to generate dataSets first.");
            return false;
        }

        String[] fileList = datasetFolder.list();
        if (fileList == null || fileList.length == 0) {
            return false;
        }

        for (String file : fileList) {
            if (file.equals(targetFileName)) {
                return true;
            }
        }
        return false;
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
        int left = 0;
        int right = list.size() - 1;
        int comparisons = 0;
        int n = list.size();

        // --------------------------------BEST CASE------------------------------
        long startTime = System.nanoTime();
        left = 0;
        right = n - 1;
        comparisons = 0;

        while (left <= right) {
            int middle = (right + left) / 2;
            comparisons++;
            break;
        }

        long endTime = System.nanoTime();
        double bestTime = (endTime - startTime) / 1_000_000.0;
        output.append(String.format("Best Case Time   : %.4f ms\n", bestTime));
        output.append("loop: " + comparisons + "\n");
        output.append("\n");

        // ---------------------------------WORST CASE-----------------------------
        int maxComparisons = 0;
        double worstTime = 0;

        for (int i = 0; i < n; i++) {
            long target = list.get(i).number;
            left = 0;
            right = n - 1;
            comparisons = 0;

            startTime = System.nanoTime();
            while (left <= right) {
                int middle = (left + right) / 2;
                comparisons++;

                if (list.get(middle).number == target) {
                    break;
                } else if (list.get(middle).number < target) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }
            endTime = System.nanoTime();

            if (comparisons > maxComparisons) {
                maxComparisons = comparisons;
                worstTime = (endTime - startTime) / 1_000_000.0;
            }
        }
        output.append(String.format("Worst Case Time  : %.4f ms\n", worstTime));
        output.append("loop: " + comparisons + "\n");
        output.append("\n");

        // ---------------------------------AVERAGE CASE-----------------------------
        long totalTime = 0;
        int totalComparisons = 0;
        for (int i = 0; i < n; i++) {
            long target = list.get(i).number;
            left = 0;
            right = n - 1;
            comparisons = 0;

            startTime = System.nanoTime();

            while (left <= right) {
                int middle = (left + right) / 2;
                comparisons++;

                if (list.get(middle).number == target) {
                    break;
                } else if (list.get(middle).number < target) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }

            endTime = System.nanoTime();
            long duration = endTime - startTime;
            totalTime += duration;
            totalComparisons += comparisons;

        }

        double averageTime = totalTime / (double) n / 1_000_000.0;
        output.append(String.format("Average Case Time: %.4f ms\n", averageTime));
        double averageComparisons = totalComparisons / (double) n;

        output.append("Average loop: " + averageComparisons + " ");
        output.append("Average time: " + averageTime / 1_000_000.0 + " ms \n");
        writeToFile("output/binary_search/binary_search_" + n + ".txt", output.toString());
        

        return totalComparisons;
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