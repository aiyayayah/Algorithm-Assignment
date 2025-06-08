import java.util.*;
import java.io.*;

public class dataSetGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // create output directory if it doesn't exist
        File folder = new File("dataSet");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // take 10 inputs
        int[] sizes = new int[10];
        for (int i = 0; i < 10; i++) {
            System.out.print("Enter number of rows for file " + (i + 1) + ": ");
            sizes[i] = scanner.nextInt();
        }
        scanner.close();

        // generate 10 datasets
        for (int i = 0; i < 10; i++) {
            int n = sizes[i];
            Set<Integer> uniqueIntegers = new LinkedHashSet<>();
            while (uniqueIntegers.size() < n) {
                int value = random.nextInt(1_000_000_000) + 1;
                uniqueIntegers.add(value);
            }

            List<String> dataset = new ArrayList<>(n);
            for (int value : uniqueIntegers) {
                String randomStr = generateRandomString(random, 5 + random.nextInt(3));
                dataset.add(value + "," + randomStr);
            }

            Collections.shuffle(dataset);

            String filename = "dataSet/dataset_" + n + ".csv";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (String line : dataset) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Dataset written to " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // generat a random lowercase string
    private static String generateRandomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = (char) ('a' + random.nextInt(26));
            sb.append(ch);
        }
        return sb.toString();
    }
}
