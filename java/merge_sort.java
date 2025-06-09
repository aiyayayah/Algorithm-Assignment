import java.io.*;
import java.util.*;

public class merge_sort {

    static class DataRow {
        int number;
        String text;

        DataRow(int number, String text) {
            this.number = number;
            this.text = text;
        }

        public String toString() {
            return number + "," + text;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user to input the file name
        System.out.print("Enter the dataset filename: ");
        String inputFile = scanner.nextLine().trim();

        List<DataRow> dataList = readCSV(inputFile);
        if (dataList == null)
            return;

        DataRow[] dataArray = dataList.toArray(new DataRow[0]);

        long startTime = System.nanoTime();
        mergeSort(dataArray, 0, dataArray.length - 1);
        long endTime = System.nanoTime();

        // Create output directory if it does not exist
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        String outputFile = "output/merge_sort_" + dataArray.length + ".csv";
        writeCSV(dataArray, outputFile);
        System.out.printf("Running time: %.3f seconds\n", (endTime - startTime) / 1e9);
    }

    // Merge Sort
    static void mergeSort(DataRow[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
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

    // Read CSV
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

    // Write CSV
    static void writeCSV(DataRow[] data, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (DataRow row : data) {
                bw.write(row.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + filename);
        }
    }
}
