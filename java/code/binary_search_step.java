import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JFileChooser;

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
            List<String> output = new ArrayList<>();
            int index = binarySearch(dataList, target, output);

            String baseDir = System.getProperty("user.dir");
            String folderPath = baseDir + File.separator + "java" + File.separator + "output" + File.separator
                    + "binary_search_step";
            File outputDir = new File(folderPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String status = (index != -1) ? "found" : "not found";
            String outputFilePath = folderPath + "/binary_search_step_" + target + ".txt (" + status + ")";
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
                for (String step : output) {
                    writer.println(step);
                }
            }

            System.out.println("Search steps written to: " + outputFilePath);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Please enter a valid number.");
        } catch (IOException e) {
            System.out.println("Error writing output file.");
        }
    }

    private static String GetUserInput() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV dataset file for binary search step");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("No file selected. Exiting.");
            System.exit(0);
            return null; // unreachable, but required
        }
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

    private static int binarySearch(List<Pair> list, long target, List<String> stepsOutput) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int middle = (left + right) / 2;
            long current = list.get(middle).number;

            String step = (middle + 1) + ": " + list.get(middle).number + "/" + list.get(middle).text;
            stepsOutput.add(step);

            if (current == target) {
                return middle;
            } else if (current > target) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        stepsOutput.add("-1");
        return -1;
    }
}
