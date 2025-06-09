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

        long fileNumber = ExtractNumberFromFileName(fileName);

        // Parse the file
        List<Pair> dataList = ParseFile("output/" + fileName);
        if (dataList == null) {
            System.out.println("Failed to read or parse the file.");
            return;
        }

        // Sort list by number for binary search
        dataList.sort(Comparator.comparingLong(p -> p.number));

        // BEST CASE: Middle element
        long bestCaseTarget = dataList.get(dataList.size() / 2).number;

        // AVERAGE CASE: Random element
        Random rand = new Random();
        long averageCaseTarget = dataList.get(rand.nextInt(dataList.size())).number;

        // WORST CASE: Target not in list
        long worstCaseTarget = -1; // Assuming all numbers in list are positive

        System.out.println("\n=== BEST CASE ===");
        measureSearchTime(dataList, bestCaseTarget);

        System.out.println("\n=== AVERAGE CASE ===");
        measureSearchTime(dataList, averageCaseTarget);

        System.out.println("\n=== WORST CASE ===");
        measureSearchTime(dataList, worstCaseTarget);
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

    private static long ExtractNumberFromFileName(String fileName) {
        try {
            String[] parts = fileName.split("_");
            String lastPart = parts[parts.length - 1];
            return Long.parseLong(lastPart.replace(".csv", ""));
        } catch (Exception e) {
            return -1;
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

    // Binary Search on List<Pair>
    private static int binarySearch(List<Pair> list, long target) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            long midValue = list.get(mid).number;

            if (midValue == target) {
                return mid;
            } else if (midValue < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1; // not found
    }

    private static void measureSearchTime(List<Pair> list, long target) {
        long startTime = System.nanoTime();
        int index = binarySearch(list, target);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        if (index != -1) {
            System.out.println(target + "," + list.get(index).text + " at index " + index);
        } else {
            System.out.println("Target " + target + " not found.");
        }
        System.out.println("Search took " + duration + " nanoseconds (" + (duration / 1_000_000.0) + " ms).");
    }
}
