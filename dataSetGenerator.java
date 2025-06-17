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
                long numRows = Long.parseLong(scanner.nextLine());

                String fileName = "dataSet/dataset_" + numRows + ".csv";
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

                // writer.newLine();

                for (long i = 0; i < numRows; i++) {
                    int number = 1_000_000_000 + random.nextInt(1_000_000_000); // 10-digit number
                    String text = generateLowercaseString(random, 10);
                    writer.write(number + "," + text);
                    writer.newLine();

                    if (i % 10_000_000 == 0 && i != 0) {
                        System.out.println("  -> " + i + " rows written to " + fileName);
                    }
                }

                writer.close();
                System.out.println("Finished writing " + numRows + " rows to " + fileName);
            }
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    // Generate a random lowercase string of given length
    public static String generateLowercaseString(Random random, int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
