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
        System.out.println("Extracted number from file name: " + fileNumber);

        // Parse the file
        List<Pair> dataList = ParseFile("output/" + fileName);
        if (dataList == null) {
            System.out.println("Failed to read or parse the file.");
            return;
        }

        // Sort list by number for binary search
        dataList.sort(Comparator.comparingLong(p -> p.number));

        // Ask user for number to search
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number to binary search: ");
        long key = scanner.nextLong();
        scanner.close();
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

    private static long ExtractNumberFromFileName(String fileName) {
        try {
            // Assuming format like "merge_sort_2342.csv"
            // Split by underscore to get the last part with number and extension
            String[] parts = fileName.split("_");
            String lastPart = parts[parts.length - 1]; // "2342.csv"
            // Remove the ".csv" extension and parse number
            return Long.parseLong(lastPart.replace(".csv", ""));
        } catch (Exception e) {
            System.out.println("Failed to extract number from file name.");
            return -1; // or handle error as needed
        }
    }

}
