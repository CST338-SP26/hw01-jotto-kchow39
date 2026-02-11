import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author feng3302
 * @version 0.1.0
 * @since 1/29/26
 **/
public class Jotto {
    private static final int WORD_SIZE = 5;
    private static final boolean DEBUG = true;

    private final ArrayList<String> playGuesses; //words guessed by player
    private final ArrayList<String> playWords; //words previously to be guessed
    private final ArrayList<String> wordList; //list of possible words

    private String filename;
    private String currentWord; //word trying to be guessed
    private int score; //player's score


    public Jotto(String filename) {
        wordList = new ArrayList<>();
        playGuesses = new ArrayList<>();
        playWords = new ArrayList<>();

        setFilename(filename);

        readWords();
    }

    /**
     * pick a word in word list and make it the current word
     * @return false if all are guessed, true else
     */
    public boolean pickWord(){
        Random r = new Random();
        currentWord = wordList.get(r.nextInt(wordList.size()));
        if(playWords.contains(currentWord)){
            if(playWords.size() == wordList.size()){
                System.out.println("You've guessed them all!");
                return false;
            } else {
                pickWord();
            }
        } else {
            playWords.add(currentWord);
            if(DEBUG){
                System.out.println(currentWord);
            }
        }
        return true;

    }

    /**
     * show the word list
     * @return a string of the words
     */
    public String showWordList(){
        StringBuilder sb = new StringBuilder();
        sb.append("Current word list:\n");
        for (String s : wordList) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

    /**
     * show the guesses the player has made. Can add those guesses to available words
     * @return the ArrayList of playGuesses
     */
    public ArrayList<String> showPlayerGuesses(){
        Scanner in = new Scanner(System.in);
        if(playGuesses.isEmpty()){
            System.out.println("No guesses yet");
            return playGuesses;
        }
        System.out.println("Current guesses: ");
        for(String p : playGuesses){
            System.out.println(p);
        }
        System.out.print("Would you like to add the words to the word list? (y/n) ");
        if(in.next().equalsIgnoreCase("y")){
            System.out.println("Updating word list.");
            updateWordList();
            showWordList();
        }
        return playGuesses;
    }

    /**
     * prints out the previous guesses and their score
     * @param guesses as ArrayList<String>
     */
    public void playerGuessScores(ArrayList<String> guesses){
        System.out.println("Guess\t\tScore");
        for (String guess : guesses) {
            System.out.printf("%s\t\t%d\n", guess, getLetterCount(guess));
        }
    }

    /**
     * Reads in the words from a file
     * @return the wordlist, an ArrayList
     */
    public ArrayList<String> readWords(){
        try {
            File f = new File(filename);
            Scanner fs = new Scanner(f);
            while(fs.hasNext()){
                String input = fs.next();
                if(!wordList.contains(input)){
                    wordList.add(input);
                }
            }

            return wordList;
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open " + filename);
        }
        return wordList;
    }

    /**
     * The UI of the game. Allows users to see stats and start the game
     */
    public void play(){
        Scanner in = new Scanner(System.in);
        System.out.print("Welcome to the game.");
        do {
            System.out.print("Current Score: " + score + "\n" +
                    "=-=-=-=-=-=-=-=-=-=-=\n" +
                    "Choose one of the following:\n" +
                    "1:\t Start the game\n" +
                    "2:\t See the word list\n" +
                    "3:\t See the chosen words\n" +
                    "4:\t Show Player guesses\n" +
                    "zz to exit\n" +
                    "=-=-=-=-=-=-=-=-=-=-=\n" +
                    "What is your choice: ");
            String userInput = in.next().toLowerCase().trim();
            if (!userInput.equals("1") && !userInput.equals("one")) {
                if (!userInput.equals("2") && !userInput.equals("two")) {
                    if (!userInput.equals("3") && !userInput.equals("three")) {
                        if (userInput.equals("4") || userInput.equals("four")){
                            showPlayerGuesses();
                        } else if (userInput.equals("zz")){
                            break;
                        } else {
                            System.out.println("I don't know what \"" + userInput + "\" is.");
                        }
                    } else {
                        System.out.println(showPlayedWords());
                    }
                } else {
                    System.out.println(showWordList());
                }
            } else {
                if (pickWord()){
                    score = guess();
                } else {
                    showPlayerGuesses();
                }
            }
            System.out.print("Press enter to continue");
            in.nextLine();
            in.nextLine();
        } while (true);
    }

    /**
     * The game - gives scores accordingly to if you guessed the word
     * @return the score
     */
    public int guess(){
        ArrayList<String> currentGuesses = new ArrayList<>(); //stores all words entered by user for current round
        Scanner scan = new Scanner(System.in); //reads user input and stores in wordGuess
        int letterCount; //counts the letters wordGuess has in common with currentWord
        int score = WORD_SIZE + 1; //score for current round
        String wordGuess; //word user has guessed

        while(true){
            System.out.println("Current Score: " + score);
            System.out.print("What is your guess (q to quit): ");
            wordGuess = scan.next();
            if(wordGuess.equals("q")){
                if(score > 0){
                    score = 0;
                }
                break;
            }
            if(wordGuess.length() != WORD_SIZE){
                System.out.println("Word must be " + WORD_SIZE + " characters (" + wordGuess + " is " + wordGuess.length() + ")");
                continue;
            }
            addPlayerGuess(wordGuess);

            if(wordGuess.equalsIgnoreCase(currentWord)){
                System.out.println("DINGDINGDING!!! the word was " + currentWord);
                currentGuesses.add(wordGuess);
                playerGuessScores(currentGuesses);
                return score;
            }

            if(currentGuesses.contains(wordGuess)){
                System.out.println("You have already played this word");
                continue;
            }

            currentGuesses.add(wordGuess);

            letterCount = getLetterCount(wordGuess);

            if(letterCount != WORD_SIZE){
                System.out.println(wordGuess + " has a Jotto score of " + letterCount);
            } else {
                System.out.println(wordGuess + " is an anagram");
            }
            score--;
            playerGuessScores(currentGuesses);
        }
        return score;
    }

    /**
     * return a string of words to print
     * @return the string of words
     */
    public String showPlayedWords(){
        if(playWords.isEmpty()){
            return "No words have been played.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Current list of played words:\n");
        for (String word : playWords){
            sb.append(word).append("\n");
        }
        return sb.toString();
    }

    /**
     * adds a player guess if not already guessed
     * @param wordGuess as a String
     * @return true if added
     */
    public boolean addPlayerGuess(String wordGuess){
        if(!playGuesses.contains(wordGuess)){
            playGuesses.add(wordGuess);
            return true;
        }
        return false;
    }

    /**
     * add play guesses to the word list and then write the word list to the original
     */
    public void updateWordList(){
        try{
          FileWriter fw = new FileWriter(filename);
            for (String playGuess : playGuesses) {
                if (!wordList.contains(playGuess)) {
                    wordList.add(playGuess);
                }
            }
            for (String s : wordList) {
                fw.write(s + "\n");
            }
          fw.close();
        } catch(IOException e) {
            System.out.println("An error occurred while writing to " + filename);
        }
    }

    /**
     * return the amount of letters the words have in common
     * @param wordGuess as a String
     * @return count as an int
     */
    public int getLetterCount(String wordGuess){
        int count = 0;
        if(wordGuess.equalsIgnoreCase(currentWord)){
            return WORD_SIZE;
        }
        wordGuess = wordGuess.toLowerCase();
        ArrayList<Character> currentWordChar = new ArrayList<>();
        for(int i = 0; i < currentWord.length(); i++){
            currentWordChar.add(currentWord.charAt(i));
        }
        for(int i = 0; i < wordGuess.length(); i++){
            if(currentWordChar.contains(wordGuess.charAt(i))){
                currentWordChar.remove(Character.valueOf(wordGuess.charAt(i)));
                count++;
            }
        }
        return count;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public ArrayList<String> getPlayedWords() {
        return playWords;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
