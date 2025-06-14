import java.io.*;
import java.util.*;

public class binary_search_step {

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

        Scanner targetObject = new Scanner(System.in);
        System.out.println("Enter a number to search");

        String targetInput = targetObject.nextLine();

        // Parse the file
        List<Pair> dataList = ParseFile("output/" + fileName);
        if (dataList == null) {
            System.out.println("Failed to read or parse the file.");
            return;
        }

        try {
            long target = Long.parseLong(targetInput); // Convert string to long
            binarySearch(dataList, target);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Please enter a valid number.");
        }

    }

    private static String GetUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the file name to perform binary search step:");
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

    private static int binarySearch(List<Pair> list, long target) {

        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int middle = (left + right) / 2;
            long current = list.get(middle).number;

            if (current == target) {
                System.out.print(middle + ": ");
                System.out.print(list.get(middle).number);
                System.out.println("/" + list.get(middle).text);
                return middle;
            }

            else if (current > target) {
                System.out.print(middle + ": ");
                System.out.print(list.get(middle).number);
                System.out.println("/" + list.get(middle).text);
                right = middle - 1;
            }

            else {
                System.out.print(middle + ": ");
                System.out.print(list.get(middle).number);
                System.out.println("/" + list.get(middle).text);
                left = middle + 1;
            }
        }
        System.out.println("-1");
        return -1;
    }

}