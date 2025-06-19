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

        File file = new File(fileName);
        String baseName = file.getName();

        if (!fileIsFound) {
            System.out.println(baseName + " was NOT FOUND at the given path.");
            return;
        }

        System.out.println(baseName + " was FOUND.");

        Scanner targetObject = new Scanner(System.in);
        System.out.println("Enter a number to search:");
        String targetInput = targetObject.nextLine();

        List<Pair> dataList = ParseFile(fileName);
        if (dataList == null) {
            System.out.println("Failed to read or parse the file.");
            return;
        }

        try {
            long target = Long.parseLong(targetInput);
            binarySearch(dataList, target);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Please enter a valid number.");
        }
    }

    private static String GetUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the full file path to perform binary search step:");
        return scannerObject.nextLine();
    }

    private static boolean SearchFileName(String fullPath) {
        File file = new File(fullPath);
        return file.exists() && file.isFile();
    }

    private static List<Pair> ParseFile(String filename) {
        List<Pair> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) {
                    System.out.println("Invalid line format: " + line);
                    continue;
                }

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

            System.out.print(middle + 1 + ": ");
            System.out.print(list.get(middle).number);
            System.out.println("/" + list.get(middle).text);

            if (current == target) {
                return middle;
            } else if (current > target) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        System.out.println("-1");
        return -1;
    }
}
