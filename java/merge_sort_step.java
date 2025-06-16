import java.io.*;
import java.util.*;

public class merge_sort_step1 {
     static class Data {
          int integer;
          String alphabet;

          Data(int integer, String alphabet){
            this.integer = integer;
            this.alphabet = alphabet;
          }
          
          @Override
          public String toString() {
               return integer + "/" + alphabet;
          }
     }

     static BufferedWriter writer;

     public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);
          System.out.print("Enter dataset file (Start with 'dataset/'): ");
          String filename = scanner.nextLine().trim();
          System.out.print("Start row: ");
          int start = scanner.nextInt();
          System.out.print("End row: ");
          int end = scanner.nextInt();

          List<Data> rows = read(filename);
          if(rows == null || start < 1 || end > rows.size()) return;

          List<Data> steps = rows.subList(start - 1, end);
          Data[] array = steps.toArray(new Data[0]);

          try {
               writer = new BufferedWriter(new FileWriter("merge_sort_step" + "_" + start + "_" + end + ".csv"));
               step(array);
               sort(array, 0, array.length-1);
               writer.close();
               System.out.println("Steps saved.");
          } catch (IOException e) {
               System.out.println("Error writting file.");
          }
     }

     static List<Data> read(String filename) {
          List<Data> list = new ArrayList<>();
          try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
               String line;
               while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    list.add(new Data(Integer.parseInt(parts[0].trim()), parts[1].trim()));
               }
          } catch (IOException e) {
               System.out.println("Failed to read file.");
               return null;
          }
          return list;
     }

     static void sort(Data[] array, int left, int right) throws IOException{
          if (left >= right) return;
          int mid = (left + right) / 2;
          sort(array, left, mid);
          sort(array, mid + 1, right);
          merge(array, left, mid, right);
          step(array);
     }

     static void merge(Data[] array, int left, int mid, int right) {
          List<Data> temp = new ArrayList<>();
          int i = left, j = mid + 1;
          
          while (i <= mid && j <= right) {
               temp.add(array[i].integer <= array[j].integer ? array[i++] : array [j++]);
          }
          while (i <= mid) {
               temp.add(array[i++]);
          }
          while (j <= right) {
               temp.add(array[j++]);
          }
          for (int k = 0; k < temp.size(); k++) {
               array[left + k] = temp.get(k);
          }
     }

     static void step(Data[] array) throws IOException{
          StringBuilder out = new StringBuilder("[");
          for (int i = 0; i < array.length; i++) {
               out.append(array[i]);
               if (i < array.length - 1)
                    out.append(", ");
          }
          out.append("]");
          writer.write(out.toString());
          writer.newLine();
     }

}
