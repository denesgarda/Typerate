package com.denesgarda.Typerate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Fetching online info...");
        String content = "";
        try {
            URLConnection connection = new URL("https://www.vocabulary.com/lists/154147").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        }
        catch(Exception e) {
            System.out.println("Could not establish a connection to the webserver. Press [ENTER] to exit.");
            in.readLine();
            System.exit(0);
        }
        System.out.println("Parsing data...");
        LinkedList<String> wordBank = new LinkedList<>();
        Scanner parser = new Scanner(content);
        while(parser.hasNext()) {
            String line = parser.nextLine();
            if(line.contains("<a class=\"word dynamictext\" href=\"/dictionary/")) {
                int beginIndex = line.indexOf(">") + 1;
                int endIndex = line.indexOf("</");
                String word = line.substring(beginIndex, endIndex);
                wordBank.add(word);
            }
        }
        while(true) {
            System.out.println("Loading...");
            Random random = new Random();
            LinkedList<String> passage = new LinkedList<>();
            for(int i = 0; i < 20; i++) {
                passage.add((String) wordBank.toArray()[random.nextInt(wordBank.size())]);
            }
            System.out.println("Type the following as fast as you can. Press [ENTER] to start. Press [ENTER] when done. No mistakes can be made.");
            in.readLine();
            String text = "";
            for(String word : passage) {
                text += word + " ";
            }
            text = text.substring(0, text.length() - 1);
            System.out.println(text);
            long startTime = System.currentTimeMillis();
            String input = in.readLine();
            long endTime = System.currentTimeMillis();
            List<String> inputtedList = Arrays.asList(input.split(" "));
            int words = 0;
            int correctCharacters = text.length();
            try {
                for(int i = 0; i < 20; i++) {
                    if(passage.get(i).equals(inputtedList.get(i))) {
                        words++;
                    }
                    else {
                        correctCharacters -= inputtedList.get(i).length();
                    }
                }
            }
            catch(IndexOutOfBoundsException e) {
                System.out.println("DNF. Results unavailable. Press [ENTER] to retry.");
            }
            long timeTaken = endTime - startTime;
            double charactersPerMinute = (double) correctCharacters / ((double)timeTaken / 60000);
            double wordsPerMinute = charactersPerMinute / 5.0;
            double accuracy = ((double) words / 20) * 100;
            System.out.println("\nRESULTS:\nWords: 20\nCorrect Words: " + words + "\nTime taken: " + ((double)timeTaken / 1000) + " seconds\nCharacters per minute: " + charactersPerMinute + "\nWords per minute: " + wordsPerMinute + "\nAccuracy: " + accuracy + "%");
            System.out.println("Press [ENTER] to retry.");
            in.readLine();
        }
    }
}
