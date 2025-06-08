import java.util.Scanner;
import java.io.File;

public class binary_search {
    public static void main(String[] args) {
        String fileName = getUserInput();
        boolean fileIsFound = searchFileName(fileName);

        if (fileIsFound) {
            System.out.println(fileName + " was FOUND in the dataset folder.");
        } else {
            System.out.println(fileName + " was NOT FOUND in the dataset folder.");
        }
    }

    private static String getUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the file name to perform binary search:");
        String fileNameInput = scannerObject.nextLine();
        return fileNameInput;
    }

    private static boolean searchFileName(String targetFileName) {
        File datasetFolder = new File("dataSet");

        if (!datasetFolder.exists() || !datasetFolder.isDirectory()) {
            System.out.println("Dataset folder not found!");
            System.out.println("Please run dataSetGenerator.java to generate dataSets first.");
            return false;
        }

        String[] fileList = datasetFolder.list();
        if (fileList == null || fileList.length == 0) {
            return false;
        }

        for (String file : fileList) {
            if (file.equals(targetFileName)) {
                return true;
            }
        }
        return false;
    }

}
