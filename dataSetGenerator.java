import java.util.*;
import java.io.*;

public class dataSetGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        File folder = new File("dataSet");
        if (!folder.exists()) {
            folder.mkdir();
        }

        int[] sizes = new int[10];
        for (int i = 0; i < 10; i++) {
            System.out.print("Enter number of rows for file " + (i + 1) + ": ");
            sizes[i] = scanner.nextInt();
            if (sizes[i] <= 0) {
                System.out.println("Please enter a positive integer.");
                i--;
            }
        }
        scanner.close();

        for (int i = 0; i < 10; i++) {
            int n = sizes[i];
            String filename = "dataSet/dataset_" + "_" + n + ".csv";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (int value = 1; value <= n; value++) {
                    String randomStr = generateRandomString(random, 5 + random.nextInt(3));
                    writer.write(value + "," + randomStr);
                    writer.newLine();
                }
                System.out.println("Dataset written to " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateRandomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = (char) ('a' + random.nextInt(26));
            sb.append(ch);
        }
        return sb.toString();
    }
}
