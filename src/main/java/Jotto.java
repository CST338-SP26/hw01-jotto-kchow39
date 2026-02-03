import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author feng3302
 * @version 0.1.0
 * @since 1/29/26
 **/
public class Jotto {
    private static final int WORD_SIZE = 5;
    private static final boolean DEBUG = true;

    private String currentWord;
    private int score;
    private ArrayList<String> playGuesses;
    private ArrayList<String> playWords;
    private ArrayList<String> wordList;
    private String filename;

    public Jotto(String filename) {
        wordList = new ArrayList<>();
        playGuesses = new ArrayList<>();
        playWords = new ArrayList<>();

        setFilename(filename);

        readWords();
    }

    public boolean pickWord(){

        return false;
    }

    public String showWordList(){

        return null;
    }

    public ArrayList<String> showPlayerGuesses(){

        return null;
    }

    public void playerGuessesScores(ArrayList<String> al){

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
                wordList.add(fs.next());
            }

            return wordList;
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open " + filename);
        }
        return wordList;
    }

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
                        showPlayedWords();
                    }
                } else {
                    showWordList();
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
        } while(true);
    }

    public int guess(){
        return 0;
    }

    public String showPlayedWords(){
        return null;
    }

    public boolean addPlayerGuess(String guess){
        return false;
    }

    public void updateWordList(){

    }

    public int getLetterCount(String wordGuess){

        return -1;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<String> getPlayGuesses() {
        return playGuesses;
    }

    public void setPlayGuesses(ArrayList<String> playGuesses) {
        this.playGuesses = playGuesses;
    }

    public ArrayList<String> getPlayedWords() {
        return playWords;
    }

    public void setPlayWords(ArrayList<String> playWords) {
        this.playWords = playWords;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public void setWordList(ArrayList<String> wordList) {
        this.wordList = wordList;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
