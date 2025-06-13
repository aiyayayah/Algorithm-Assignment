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
        String fileName = GetUserInput();
        boolean fileIsFound = SearchFileName(fileName);

        if (!fileIsFound) {
            System.out.println(fileName + " was NOT FOUND in the output folder.");
            return;
        }

        System.out.println(fileName + " was FOUND in the output folder.");

        // Parse the file
        List<Pair> dataList = ParseFile("output/" + fileName);
        if (dataList == null) {
            System.out.println("Failed to read or parse the file.");
            return;
        }
        binarySearch(dataList);
    }

    private static String GetUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the file name to perform binary search:");
        return scannerObject.nextLine();
    }

    private static boolean SearchFileName(String targetFileName) {
        File datasetFolder = new File("output");

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

    // Binary Search on List<Pair>
    private static int binarySearch(List<Pair> list) {
        int left = 0;
        int right = list.size() - 1;
        int comparisons = 0;
        int n = list.size();

        System.out.println("Total elements: " + n);
        System.out.println(right);

        // BEST CASE - Middle element
        System.out.println("BEST CASE:");
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
        double bestTime = (endTime - startTime);
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Time: " + bestTime + " nanoseconds");
        System.out.println();

        // WORST CASE
        System.out.println("WORST CASE (Leftmost vs Rightmost):");

        // target at LEFTMOST of the list
        startTime = System.nanoTime();
        left = 0;
        right = list.size() - 1;
        comparisons = 0;
        while (left <= right) {
            int middle = (left + right) / 2;
            comparisons++;
            if (middle == 0)
                break;
            right = middle - 1;
        }
        endTime = System.nanoTime();
        double leftTime = endTime - startTime;
        int leftComparisons = comparisons;

        System.out.println("Leftmost Target:");
        System.out.println("  Loop: " + leftComparisons);
        System.out.println("  Time: " + leftTime + " nanoseconds");

        // target at RIGHTMOST of the list
        startTime = System.nanoTime();
        left = 0;
        right = list.size() - 1;
        comparisons = 0;
        while (left <= right) {
            int middle = (left + right) / 2;
            comparisons++;
            if (middle == n - 1)
                break;
            left = middle + 1;
        }
        endTime = System.nanoTime();
        double rightTime = endTime - startTime;
        int rightComparisons = comparisons;

        System.out.println("Rightmost Target:");
        System.out.println("  Loop: " + rightComparisons);
        System.out.println("  Time: " + rightTime + " nanoseconds");

        System.out.println();
        if (leftTime > rightTime) {
            System.out.println("True worst case: Leftmost");
        } else if (rightComparisons > leftComparisons) {
            System.out.println("True worst case: Rightmost");
        } else {
            // Time comparison if comparisons are equal
            if (leftTime > rightTime) {
                System.out.println("True worst case: Leftmost (slower time)");
            } else {
                System.out.println("True worst case: Rightmost (slower time)");
            }
        }
        System.out.println();

        // AVERAGE CASE - Simulate multiple searches
        System.out.println("AVERAGE CASE:");
        long totalTime = 0;
        int totalComparisons = 0;
        int testRuns = n; // Test n different scenarios

        for (int test = 0; test < testRuns; test++) {
            startTime = System.nanoTime();
            left = 0;
            right = n - 1;
            comparisons = 0;

            // Simulate different search depths
            int targetDepth = (test % (int) Math.ceil(Math.log(n) / Math.log(2))) + 1;

            while (left <= right && comparisons < targetDepth) {
                int middle = (right + left) / 2;
                comparisons++;

                // Simulate different search patterns
                if (test % 2 == 0) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }

            endTime = System.nanoTime();
            totalTime += (endTime - startTime);
            totalComparisons += comparisons;
        }

        double averageTime = (totalTime / (double) testRuns);
        double averageComparisons = totalComparisons / (double) testRuns;

        System.out.println("Average comparisons: " + String.format("%.2f", averageComparisons));
        System.out.println("Average time: " + String.format("%.9f", averageTime) + " nanoseconds");
        System.out.println();

        return comparisons;
    }
}