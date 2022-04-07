package dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryTimeTest {

    public static void main(String[] args) throws FileNotFoundException {
        sortedArrayTest();
        System.out.println("---------------------------------");
        hashTest();
    }

    private static void sortedArrayTest() throws FileNotFoundException {
        Scanner scanner1 = new Scanner(new File("src/dictionary/dtengl.txt"));
        long startTime1 = System.currentTimeMillis();
        Dictionary<String, String> dict1 = new SortedArrayDictionary<>();
        for (int i = 0; i < 8000; i++) {
            if (scanner1.hasNextLine()) {
                String[] currentArgs = scanner1.nextLine().split(" ");
                dict1.insert(currentArgs[0], currentArgs[1]);
            }
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("SortedArrayDictionary: Aufbau für 8000 Einträge = " + (endTime1 - startTime1) + "ms");

        Scanner scanner2 = new Scanner(new File("src/dictionary/dtengl.txt"));
        long startTime2 = System.currentTimeMillis();
        Dictionary<String, String> dict2 = new SortedArrayDictionary<>();
        for (int i = 0; i < 16000; i++) {
            if (scanner2.hasNextLine()) {
                String[] currentArgs = scanner2.nextLine().split(" ");
                dict2.insert(currentArgs[0], currentArgs[1]);
            }
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("SortedArrayDictionary: Aufbau für 16000 Einträge = " + (endTime2 - startTime2) + "ms");

        Scanner scanner3 = new Scanner(new File("src/dictionary/dtengl.txt"));
        ArrayList<String> list8k = new ArrayList<>();
        for (int i = 0; i < 8000; i++) {
            if (scanner3.hasNextLine()) {
                String[] currentArgs = scanner3.nextLine().split(" ");
                list8k.add(currentArgs[0]);
            }
        }
        Scanner scanner4 = new Scanner(new File("src/dictionary/dtengl.txt"));
        ArrayList<String> list16k = new ArrayList<>();
        for (int i = 0; i < 16000; i++) {
            if (scanner4.hasNextLine()) {
                String[] currentArgs = scanner4.nextLine().split(" ");
                list16k.add(currentArgs[0]);
            }
        }
        Scanner scanner5 = new Scanner(new File("src/dictionary/dtengl.txt"));
        Dictionary<String, String> dict3 = new SortedArrayDictionary<>();
        for (int i = 0; i < 8000; i++) {
            if (scanner5.hasNextLine()) {
                String[] currentArgs = scanner5.nextLine().split(" ");
                dict3.insert(currentArgs[0], currentArgs[1]);
            }
        }
        Scanner scanner6 = new Scanner(new File("src/dictionary/dtengl.txt"));
        Dictionary<String, String> dict4 = new SortedArrayDictionary<>();
        for (int i = 0; i < 8000; i++) {
            if (scanner6.hasNextLine()) {
                String[] currentArgs = scanner6.nextLine().split(" ");
                dict4.insert(currentArgs[0], currentArgs[1]);
            }
        }
        long startTime3 = System.currentTimeMillis();
        for (String s : list8k) {
            dict3.search(s);
        }
        long endTime3 = System.currentTimeMillis();
        System.out.println("SortedArrayDictionary: Suchen für 8000 Einträge = " + (endTime3 - startTime3) + "ms");

        long startTime4 = System.currentTimeMillis();
        for (String s : list16k) {
            dict4.search(s);
        }
        long endTime4 = System.currentTimeMillis();
        System.out.println("SortedArrayDictionary: Suchen für 16000 Einträge = " + (endTime4 - startTime4) + "ms");
    }

    private static void hashTest() throws FileNotFoundException {
        Scanner scanner1 = new Scanner(new File("src/dictionary/dtengl.txt"));
        long startTime1 = System.currentTimeMillis();
        Dictionary<String, String> dict1 = new HashDictionary<>(3);
        for (int i = 0; i < 8000; i++) {
            if (scanner1.hasNextLine()) {
                String[] currentArgs = scanner1.nextLine().split(" ");
                dict1.insert(currentArgs[0], currentArgs[1]);
            }
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("HashDictionary: Aufbau für 8000 Einträge = " + (endTime1 - startTime1) + "ms");

        Scanner scanner2 = new Scanner(new File("src/dictionary/dtengl.txt"));
        long startTime2 = System.currentTimeMillis();
        Dictionary<String, String> dict2 = new HashDictionary<>(3);
        for (int i = 0; i < 16000; i++) {
            if (scanner2.hasNextLine()) {
                String[] currentArgs = scanner2.nextLine().split(" ");
                dict2.insert(currentArgs[0], currentArgs[1]);
            }
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("HashDictionary: Aufbau für 16000 Einträge = " + (endTime2 - startTime2) + "ms");

        Scanner scanner3 = new Scanner(new File("src/dictionary/dtengl.txt"));
        ArrayList<String> list8k = new ArrayList<>();
        for (int i = 0; i < 8000; i++) {
            if (scanner3.hasNextLine()) {
                String[] currentArgs = scanner3.nextLine().split(" ");
                list8k.add(currentArgs[0]);
            }
        }
        Scanner scanner4 = new Scanner(new File("src/dictionary/dtengl.txt"));
        ArrayList<String> list16k = new ArrayList<>();
        for (int i = 0; i < 16000; i++) {
            if (scanner4.hasNextLine()) {
                String[] currentArgs = scanner4.nextLine().split(" ");
                list16k.add(currentArgs[0]);
            }
        }
        Scanner scanner5 = new Scanner(new File("src/dictionary/dtengl.txt"));
        Dictionary<String, String> dict3 = new HashDictionary<>(3);
        for (int i = 0; i < 8000; i++) {
            if (scanner5.hasNextLine()) {
                String[] currentArgs = scanner5.nextLine().split(" ");
                dict3.insert(currentArgs[0], currentArgs[1]);
            }
        }
        Scanner scanner6 = new Scanner(new File("src/dictionary/dtengl.txt"));
        Dictionary<String, String> dict4 = new HashDictionary<>(3);
        for (int i = 0; i < 8000; i++) {
            if (scanner6.hasNextLine()) {
                String[] currentArgs = scanner6.nextLine().split(" ");
                dict4.insert(currentArgs[0], currentArgs[1]);
            }
        }
        long startTime3 = System.currentTimeMillis();
        for (String s : list8k) {
            dict3.search(s);
        }
        long endTime3 = System.currentTimeMillis();
        System.out.println("HashDictionary: Suchen für 8000 Einträge = " + (endTime3 - startTime3) + "ms");

        long startTime4 = System.currentTimeMillis();
        for (String s : list16k) {
            dict4.search(s);
        }
        long endTime4 = System.currentTimeMillis();
        System.out.println("HashDictionary: Suchen für 16000 Einträge = " + (endTime4 - startTime4) + "ms");
    }
}
