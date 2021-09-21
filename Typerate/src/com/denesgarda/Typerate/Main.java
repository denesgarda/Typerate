package com.denesgarda.Typerate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int wordsPerPassage = 20;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Fetching online info...");
        String content = "";
        try {
            URLConnection connection = new URL("https://www.vocabulary.com/lists/154147").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (Exception e) {
            System.out.println("Could not establish a connection to the webserver. Press [ENTER] to exit.");
            in.readLine();
            System.exit(0);
        }
        System.out.println("Parsing data...");
        LinkedList<String> wordBank = new LinkedList<>();
        Scanner parser = new Scanner(content);
        while (parser.hasNext()) {
            String line = parser.nextLine();
            if (line.contains("<a class=\"word dynamictext\" href=\"/dictionary/")) {
                int beginIndex = line.indexOf(">") + 1;
                int endIndex = line.indexOf("</");
                String word = line.substring(beginIndex, endIndex);
                wordBank.add(word);
            }
        }
        mainMenuLoop:
        while (true) {
            System.out.println("\nTyperate v1.5\n- Press [ENTER] to start\n- Type /settings to change settings\n- Type /exit to exit");
            String mainMenuInput = in.readLine();
            if (mainMenuInput.equalsIgnoreCase("/exit")) {
                break mainMenuLoop;
            } else if (mainMenuInput.equalsIgnoreCase("/settings")) {
                settingsLoop:
                while (true) {
                    System.out.println("\nSettings\n1) Words per passage: " + wordsPerPassage + "\n\nTo change a setting, type the list number. type /back or press [ENTER] to go back to main menu. Type /exit to exit.");
                    String settingsInput = in.readLine();
                    if (settingsInput.equalsIgnoreCase("1")) {
                        System.out.print("(" + wordsPerPassage + ") Enter new value: ");
                        String newValue = in.readLine();
                        try {
                            int intValue = Integer.parseInt(newValue);
                            if (intValue > 0) {
                                wordsPerPassage = intValue;
                            } else {
                                System.out.println("value must be greater than 0.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid integer value.");
                        }
                    } else if (settingsInput.equalsIgnoreCase("/back")) {
                        break settingsLoop;
                    } else if (settingsInput.equalsIgnoreCase("/exit")) {
                        break mainMenuLoop;
                    } else {
                        break settingsLoop;
                    }
                }
            } else {
                gameLoop:
                while (true) {
                    System.out.println("Loading...");
                    Random random = new Random();
                    LinkedList<String> passage = new LinkedList<>();
                    for (int i = 0; i < wordsPerPassage; i++) {
                        passage.add((String) wordBank.toArray()[random.nextInt(wordBank.size())]);
                    }
                    System.out.println("Type the following as fast as you can. Press [ENTER] to start. Press [ENTER] when done. Type /back to go back to main menu. Type /exit to exit.");
                    String startGameInput = in.readLine();
                    if (startGameInput.equalsIgnoreCase("/back")) {
                        break gameLoop;
                    } else if (startGameInput.equalsIgnoreCase("/exit")) {
                        break mainMenuLoop;
                    }
                    String text = "";
                    for (String word : passage) {
                        text += word + " ";
                    }
                    text = text.substring(0, text.length() - 1);
                    System.out.println(text);
                    long startTime = System.currentTimeMillis();
                    String input = in.readLine();
                    long endTime = System.currentTimeMillis();
                    List<String> inputtedList = Arrays.asList(input.split(" "));
                    int words = 0;
                    int correctCharacters = text.length() - (wordsPerPassage - 1);
                    try {
                        for (int i = 0; i < wordsPerPassage; i++) {
                            if (passage.get(i).equals(inputtedList.get(i))) {
                                words++;
                            } else {
                                correctCharacters -= passage.get(i).length();
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("DNF. Results unavailable. Press [ENTER] to retry. Type /back to go back to main menu. Type /exit to exit.");
                        String endGameInput = in.readLine();
                        if (endGameInput.equalsIgnoreCase("/back")) {
                            break gameLoop;
                        } else if (endGameInput.equalsIgnoreCase("/exit")) {
                            break mainMenuLoop;
                        }
                        continue;
                    }
                    long timeTaken = endTime - startTime;
                    double charactersPerMinute = (double) correctCharacters / ((double) timeTaken / 60000);
                    double wordsPerMinute = charactersPerMinute / 5.0;
                    double accuracy = ((double) words / wordsPerPassage) * 100;
                    System.out.println("\nRESULTS:\nWords: " + wordsPerPassage + "\nCorrect Words: " + words + "\nTime taken: " + ((double) timeTaken / 1000) + " seconds\nCharacters per minute: " + charactersPerMinute + "\nWords per minute: " + wordsPerMinute + "\nAccuracy: " + accuracy + "%");
                    System.out.println("Press [ENTER] to retry. Type /back to go back to main menu. Type /exit to exit.");
                    String endGameInput = in.readLine();
                    if (endGameInput.equalsIgnoreCase("/back")) {
                        break gameLoop;
                    } else if (endGameInput.equalsIgnoreCase("/exit")) {
                        break mainMenuLoop;
                    }
                }
            }
        }
    }
}
