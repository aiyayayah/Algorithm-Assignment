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
        List<Pair> dataList = ParseFile("output/merge_sort/" + fileName);
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
        File datasetFolder = new File("output/merge_sort/");

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

        System.out.println("Total elements: " + n);
        System.out.println(right);

        // BEST CASE - Middle element
        output.append("BEST CASE: \n");
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
        output.append("Comparisons: " + comparisons + "\n");
        output.append("Time: " + bestTime + " nanoseconds \n \n");

        // WORST CASE
        output.append("WORST CASE: \n");

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

        output.append("Leftmost Target: \n");
        output.append("  Loop: " + leftComparisons + "\n");
        output.append("  Time: " + leftTime + " nanoseconds \n");

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

        output.append("Rightmost Target: \n");
        output.append("  Loop: " + rightComparisons + "\n");
        output.append("  Time: " + rightTime + " nanoseconds \n");

        // target at left beside the first middle
        startTime = System.nanoTime();
        left = 0;
        right = list.size() - 1;
        int firstMiddle = (left + right) / 2;
        int targetLeftIndex = firstMiddle - 1; // One index left of the initial middle
        comparisons = 0;
        while (left <= right) {
            int middle = (left + right) / 2;
            comparisons++;
            if (middle == targetLeftIndex)
                break;
            left = middle + 1;
        }
        endTime = System.nanoTime();
        double leftBesideMiddleTime = endTime - startTime;
        int leftBesideMiddleComparisons = comparisons;

        output.append("Left Beside Middle: \n");
        output.append("  Loop: " + leftBesideMiddleComparisons + "\n");
        output.append("  Time: " + leftBesideMiddleTime + " nanoseconds \n");

        // target at right beside the first middle
        startTime = System.nanoTime();
        left = 0;
        right = list.size() - 1;
        int targetRightIndex = firstMiddle + 1; // One index right of the initial middle
        comparisons = 0;
        while (left <= right) {
            int middle = (left + right) / 2;
            comparisons++;
            if (middle == targetRightIndex)
                break;
            right = middle - 1;
        }
        endTime = System.nanoTime();
        double rightBesideMiddleTime = endTime - startTime;
        int rightBesideMiddleComparisons = comparisons;

        output.append("Right Beside Middle: \n");
        output.append("  Loop: " + rightBesideMiddleComparisons + "\n");
        output.append("  Time: " + rightBesideMiddleTime + " nanoseconds \n \n");

        // AVERAGE CASE
        output.append("AVERAGE CASE: \n");
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
            // Print and write individual search stats
            // output.append("i:" + i + " " + target + ": ");
            // output.append("Loop = " + comparisons + ", ");
            // output.append("Time = " + duration + " ns\n");
        }
        double averageTime = totalTime / (double) n;
         double averageComparisons = totalComparisons / (double) n;

        output.append("Average comparisons: " + averageComparisons + "\n");
        output.append("Average time: " + averageTime + " nanoseconds\n");
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