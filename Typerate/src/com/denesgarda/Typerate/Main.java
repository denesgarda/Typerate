package com.denesgarda.Typerate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

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
            if(input.equals(text)) {
                long timeTaken = endTime - startTime;
                double charactersPerMinute = (double) text.length() / ((double)timeTaken / 60000);
                double wordsPerMinute = charactersPerMinute / 5.0;
                System.out.println("\nRESULTS:\nWords: 20\nTime taken: " + ((double)timeTaken / 1000) + " seconds\nCharacters per minute: " + charactersPerMinute + "\nWords per minute: " + wordsPerMinute);
                System.out.println("Press [ENTER] to retry.");
                in.readLine();
            }
            else {
                System.out.println("Not accurate. Press [ENTER] to retry.");
                in.readLine();
            }
        }
    }
}
