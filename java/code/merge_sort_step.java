import java.io.*;
import java.util.*;

public class merge_sort_step {

    static class DataRow {
        int number;
        String text;

        DataRow(int number, String text) {
            this.number = number;
            this.text = text;
        }

        @Override
        public String toString() {
            return number + "/" + text;
        }
    }

    static BufferedWriter stepWriter;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user input
        System.out.print("Enter dataset filename: ");
        String inputFile = "dataset/" + scanner.nextLine().trim();

        System.out.print("Enter start row (e.g., 1): ");
        int startRow = scanner.nextInt();

        System.out.print("Enter end row (e.g., 7): ");
        int endRow = scanner.nextInt();

        List<DataRow> fullList = readCSV(inputFile);
        if (fullList == null || startRow < 1 || endRow > fullList.size() || startRow > endRow) {
            System.out.println("Invalid file or row range.");
            return;
        }

        List<DataRow> sublist = fullList.subList(startRow - 1, endRow); // 0-indexed
        DataRow[] array = sublist.toArray(new DataRow[0]);

        String folderPath = "output/merge_sort_step";
        new File(folderPath).mkdirs(); // Ensure directory exists

        String outputFile = folderPath + "/merge_sort_step_" + startRow + "_" + endRow + ".txt";

        try {
            stepWriter = new BufferedWriter(new FileWriter(outputFile));
            writeStep(array); // Initial state
            mergeSort(array, 0, array.length - 1);
            stepWriter.close();
            System.out.println("Step output written to " + outputFile);
        } catch (IOException e) {
            System.out.println("Error writing steps: " + e.getMessage());
        }

    }

    static void mergeSort(DataRow[] arr, int left, int right) throws IOException {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
            writeStep(arr);
        }
    }

    static void merge(DataRow[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        DataRow[] L = new DataRow[n1];
        DataRow[] R = new DataRow[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].number <= R[j].number) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1)
            arr[k++] = L[i++];
        while (j < n2)
            arr[k++] = R[j++];
    }

    static void writeStep(DataRow[] arr) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1)
                sb.append(", ");
        }
        sb.append("]");
        stepWriter.write(sb.toString());
        stepWriter.newLine();
    }

    static List<DataRow> readCSV(String filename) {
        List<DataRow> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                int number = Integer.parseInt(parts[0].trim());
                String text = parts[1].trim();
                list.add(new DataRow(number, text));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            return null;
        }
        return list;
    }
}
