import java.io.*;
import java.util.*;

public class dataSetGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        File outputDir = new File("dataSet");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try {
            for (int fileNum = 1; fileNum <= 10; fileNum++) {
                System.out.print("Enter number of rows for file " + fileNum + ": ");
                int numRows = Integer.parseInt(scanner.nextLine()); // Changed to int for HashSet safety

                if (numRows > 1_000_000_000) {
                    System.out.println("Too many rows. Must be <= 1,000,000,000 for uniqueness.");
                    continue;
                }

                Set<Integer> generatedNumbers = new HashSet<>(numRows);

                String fileName = "dataSet/dataset_" + numRows + ".csv";
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

                int written = 0;
                while (written < numRows) {
                    int number = random.nextInt(1_000_000_000);
                    if (generatedNumbers.add(number)) { // add returns false if already present
                        String text = generateLowercaseString(random, 10);
                        writer.write(number + "," + text);
                        writer.newLine();
                        written++;

                        if (written % 10_000_000 == 0) {
                            System.out.println("  -> " + written + " rows written to " + fileName);
                        }
                    }
                }

                writer.close();
                System.out.println("Finished writing " + numRows + " unique rows to " + fileName);
            }
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    public static String generateLowercaseString(Random random, int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
