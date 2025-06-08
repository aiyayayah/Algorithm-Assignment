import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
            System.out.println(fileName + " was NOT FOUND in the dataset folder.");
            return;
        }

        System.out.println(fileName + " was FOUND in the dataset folder.");
        List<Pair> dataList = ParseFile(fileName);
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("No data found in file.");
            return;
        }

        // Sort list by number
        dataList.sort(Comparator.comparingLong(p -> p.number));

        // Define best, average, and worst cases:
        long bestCaseTarget = dataList.get(dataList.size() / 2).number; // Middle element
        long averageCaseTarget = dataList.get(dataList.size() / 4).number; // Quarter element
        long worstCaseTarget = -1L; // A number NOT in the list (assuming all numbers positive)

        StringBuilder outputBuilder = new StringBuilder();

        // Best case timing
        long start = System.nanoTime();
        int bestIdx = binarySearch(dataList, bestCaseTarget);
        long end = System.nanoTime();
        double bestTimeSeconds = (end - start) / 1_000_000_000.0;
        outputBuilder.append("Best case (middle element):\n");
        outputBuilder.append("Target: ").append(bestCaseTarget).append("\n");
        outputBuilder.append("Found: ").append(bestIdx != -1 ? dataList.get(bestIdx).text : "Not found").append("\n");
        outputBuilder.append("Time (seconds): ").append(bestTimeSeconds).append("\n\n");

        // Average case timing
        start = System.nanoTime();
        int averageIdx = binarySearch(dataList, averageCaseTarget);
        end = System.nanoTime();
        double averageTimeSeconds = (end - start) / 1_000_000_000.0;
        outputBuilder.append("Average case (quarter element):\n");
        outputBuilder.append("Target: ").append(averageCaseTarget).append("\n");
        outputBuilder.append("Found: ").append(averageIdx != -1 ? dataList.get(averageIdx).text : "Not found")
                .append("\n");
        outputBuilder.append("Time (seconds): ").append(averageTimeSeconds).append("\n\n");

        // Worst case timing (target not in list)
        start = System.nanoTime();
        int worstIdx = binarySearch(dataList, worstCaseTarget);
        end = System.nanoTime();
        double worstTimeSeconds = (end - start) / 1_000_000_000.0;
        outputBuilder.append("Worst case (target not in list):\n");
        outputBuilder.append("Target: ").append(worstCaseTarget).append("\n");
        outputBuilder.append("Found: ").append(worstIdx != -1 ? dataList.get(worstIdx).text : "Not found").append("\n");
        outputBuilder.append("Time (seconds): ").append(worstTimeSeconds).append("\n\n");

        System.out.println(outputBuilder.toString());

        // Write output to file
        try (PrintWriter writer = new PrintWriter(new FileWriter("binary_search_n.txt"))) {
            writer.print(outputBuilder.toString());
        } catch (IOException e) {
            System.out.println("Error writing to output file.");
            e.printStackTrace();
        }
    }

    private static String GetUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the file name to perform binary search:");
        return scannerObject.nextLine();
    }

    private static boolean SearchFileName(String targetFileName) {
        File datasetFolder = new File("dataSet");

        if (!datasetFolder.exists() || !datasetFolder.isDirectory()) {
            System.out.println("Dataset folder not found!");
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

    private static List<Pair> ParseFile(String fileName) {
        List<Pair> list = new ArrayList<>();
        try {
            File myObj = new File("dataSet/" + fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(",");
                if (parts.length == 2) {
                    try {
                        long num = Long.parseLong(parts[0].trim());
                        String text = parts[1].trim();
                        list.add(new Pair(num, text));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid line: " + data);
                    }
                } else {
                    System.out.println("Skipping invalid line: " + data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }
        return list;
    }

    private static int binarySearch(List<Pair> dataList, long target) {
        int left = 0, right = dataList.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long midVal = dataList.get(mid).number;
            if (midVal == target) {
                return mid;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Not found
    }
}
