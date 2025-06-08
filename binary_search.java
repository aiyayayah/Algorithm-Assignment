import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class binary_search {
    public static void main(String[] args) {
        String fileName = GetUserInput();
        boolean fileIsFound = SearchFileName(fileName);

        if (fileIsFound) {
            System.out.println(fileName + " was FOUND in the dataset folder.");
        } else {
            System.out.println(fileName + " was NOT FOUND in the dataset folder.");
        }

        System.out.println("test: " + fileName);
        ParseFile(fileName);

    }

    private static String GetUserInput() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter the file name to perform binary search:");
        String fileNameInput = scannerObject.nextLine();
        return fileNameInput;
    }

    private static boolean SearchFileName(String targetFileName) {
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

    private static void ParseFile(String fileName) {
        try {
            File myObj = new File("dataSet/" + fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

        }
    }
}
