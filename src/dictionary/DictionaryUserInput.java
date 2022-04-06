package dictionary;

import java.io.*;
import java.util.Scanner;

public class DictionaryUserInput {
    private static Dictionary<String, String> dict;

    public static void main(String[] args) {
        boolean exit = false;
        Scanner in = new Scanner(System.in);

        System.out.println("Dictionary Text Interface:");

        while (!exit) {
            System.out.println("...");
            String answer = in.nextLine();
            String[] answerArgs = answer.split(" ");
            switch (answerArgs[0]) {
                case "create":
                    if (answerArgs.length == 1)
                        createDict("SortedArrayDictionary");
                    else
                        createDict(answerArgs[1]);
                    break;
                case "read":
                    read(answerArgs);
                    break;
                case "p":
                    if (dict.size() != 0) {
                        for (Dictionary.Entry<String, String> e : dict) {
                            System.out.println(e.getKey() + ": " + e.getValue());
                        }
                    } else System.err.println("Dictionary is empty, nothing to display!");
                    break;
                case "s":
                    if (answerArgs.length == 2) {
                        System.out.println("Englisches Wort fuer: " + answerArgs[1] + " -- " + dict.search(answerArgs[1]));
                    } else System.err.println("Wrong input.. 1 argument required.");
                    break;
                case "i":
                    if (answerArgs.length == 3) {
                        dict.insert(answerArgs[1], answerArgs[2]);
                    } else System.err.println("Wrong input.. 2 arguments required.");
                    break;
                case "r":
                    if (answerArgs.length == 2) {
                        if (dict.remove(answerArgs[1]) != null)
                            System.out.println("Removed " + answerArgs[1]);
                        else System.err.println(answerArgs[1] + " is not in the dictionary!");
                    } else System.err.println("Wrong input.. 1 argument required.");
                    break;
                case "exit":
                default:
                    exit = true;
                    break;
            }
        }

        in.close();
    }

    private static void createDict(String s) {
        switch (s) {
            case "SortedArrayDictionary" -> {
                dict = new SortedArrayDictionary<>();
                System.out.println("Created new " + s + ".");
            }
            case "HashDictionary" -> {
                dict = new HashDictionaryTest<>();
                System.out.println("Created new " + s + ".");
            }
            case "BinaryTreeDictionary" -> {
                dict = new BinaryTreeDictionary<>();
                System.out.println("Created new " + s + ".");
            }
            default -> System.out.println("Wrong input for: Dictionary Type");
        }
    }

    private static void read(String[] answerArgs) {
        try {
            int n = Integer.parseInt(answerArgs[1]);
            try {
                Scanner scanner = new Scanner(new File("src/dictionary/" + answerArgs[2]));
                for (int i = 0; i < n; i++) {
                    if (scanner.hasNextLine()) {
                        String[] currentArgs = scanner.nextLine().split(" ");
                        dict.insert(currentArgs[0], currentArgs[1]);
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found!");
            }
        } catch (NumberFormatException e1) {
            try {
                Scanner scanner = new Scanner(new File("src/dictionary/" + answerArgs[1]));
                while (scanner.hasNextLine()) {
                    String[] currentArgs = scanner.nextLine().split(" ");
                    dict.insert(currentArgs[0], currentArgs[1]);
                }
            } catch (FileNotFoundException e2) {
                System.err.println("File not found!");
            }
        }
    }
}
