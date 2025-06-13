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
            long number = Long.parseLong(lastPart.replace(".csv", ""));
            System.out.println("Extracted number from filename: " + number);
            return number;
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
    // private static int binarySearch(List<Pair> list, ) {

    // }

}
