import java.io.*;
import java.util.*;

public class merge_sort1 {
    static class Data {
        int integer;
        String alphabet;

        Data(int integer, String alphabet){
            this.integer = integer;
            this.alphabet = alphabet;
        }
        @Override
        public String toString() {
            return integer + "," + alphabet;
        }
    }   
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter dataset filename (Start with 'dataset/'): ");
        String filename = input.nextLine().trim();

        List<Data> datas = loadDatas(filename);
        if(datas == null)
            return;
        
        Data[] array = datas.toArray(new Data[0]);

        long startTime = System.nanoTime();
        sort(array, 0, array.length-1);
        long endTime = System.nanoTime();

        String outputName = "merge_sort_" + array.length + ".csv";
        saveToFile(array, outputName);

        System.out.printf("Sorted file saved to "+ outputName + "\n");
        System.out.printf("Running time: " + (endTime - startTime) / 1e9 + " seconds");
    }

    static List<Data> loadDatas(String filename){
        List <Data> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",", 2);
                list.add(new Data(Integer.parseInt(split[0].trim()), split[1].trim())); 
            }
        } catch (Exception e) {
            System.out.println("Error reading file.");
            return null;
        }
        return list;
    }

    static void saveToFile(Data[] array, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Data data : array) {
                writer.write(data.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error to write the file.");
        } 
    }

    static void sort(Data[] array, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        // System.out.printf("merge(l=%d, m=%d, r=%d)\n", left, mid, right);
        // System.out.printf("Left: %d to %d, Right: %d to %d\n", left, mid, mid+1, right);
        sort(array, left, mid);
        sort(array, mid+1, right);
        merge(array, left, mid, right);
    }

    static void merge(Data[] array, int left, int mid, int right) {
        Data[] temp = new Data[right - left + 1];
        int i = left, j = mid+1, k = 0;

        while (i <= mid && j <= right) {
            temp[k++] = (array[i].integer <= array[j].integer) ? array[i++] : array[j++];
        }
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= right) 
            temp[k++] = array[j++];
        
        for(int t = 0; t < temp.length; t++){
            array[left + t] = temp[t];
        }
    }
}



