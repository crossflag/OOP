package sentimentanalysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
  Main Application for Assignment 2
  @author vangelis
  @author jelena
  @author junye
 */
public class MainApp {


    // Used to read from System's standard input
    private static final Scanner CONSOLEINPUT = new Scanner(System.in);

    private static final TweetHandler HANDLER = new TweetHandler();
    /**
     * Main method demonstrates how to use Stanford NLP library classifier.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Let user input the path of database file.
        System.out.println("Welcome to the Sentiment Analyzer application.");

        // Load the database first.
        HANDLER.loadSerialDB();

        String welcomeMessage = "\nChoose one of the following functions:\n\n"
                + "\t 0. Exit program.\n"
                + "\t 1. Load new tweet text file.\n"
                + "\t 2. Classify tweets using NLP library and report accuracy.\n"
                + "\t 3. Manually change tweet class label.\n"
                + "\t 4. Add new tweets to database.\n"
                + "\t 5. Delete tweet from database (given its id).\n"
                + "\t 6. Search tweets by user, date, flag, or a matching substring.\n";

        System.out.println(HANDLER.getTweetBuffer().size() + " tweets in temporary buffer.");
        System.out.println(HANDLER.getTweetDB().size() + " tweets in database.");
        System.out.println(welcomeMessage);
        String selection = CONSOLEINPUT.nextLine();

        while (!selection.equals("0")) {

            switch (selection) {
                case "1":
                    // 1. Load new tweet text file.
                    loadTweets();
                    break;
                case "2":
                    // 2. Classify tweets using NLP library and report accuracy.
                    classifyTweets();
                    break;
                case "3":
                    // 3. Manually change tweet class label.
                    changeTweet();
                    break;
                case "4":
                    // 4. Add new tweets to database.
                    addTweets();
                    break;
                case "5":
                    // 5. Delete tweet from database (given its id).
                    deleteTweet();
                    break;
                case "6":
                    // 6. Search tweets by user, date, flag, or a matching substring.
                    List<AbstractTweet> searchResults = searchTweet();

                    if (searchResults.isEmpty()) {
                        System.out.println("0 result found.");
                    } else {
                        System.out.println(searchResults.size() + " result found.");
                        printList(searchResults);
                    }
                    break;
                case "h":
                    System.out.println(welcomeMessage);
                    break;
                default:
                    System.out.println("That is not a recognized command. Please enter another command or 'h' to list the commands.");
                    break;

            }

            System.out.println(HANDLER.getTweetBuffer().size() + " tweets in temporary buffer.");
            System.out.println(HANDLER.getTweetDB().size() + " tweets in database.");
            System.out.println("Please enter another command or 'h' to list the commands.\n");

            selection = CONSOLEINPUT.nextLine();
        }

        // Save the database when exit.
        HANDLER.saveSerialDB();
        System.out.println("See you!");

    }

    /**
     * Method 1: load new tweet text file.
     *
     */
    public static void loadTweets() {
        System.out.println("Please input the path to tweet csv file.");
        System.out.println("(Example: Data\\testdata.manual.2009.06.14.csv)");
        String path = MainApp.CONSOLEINPUT.nextLine();
        HANDLER.setTweetBuffer(HANDLER.loadTweetsFromText(path));
    }

    /**
     * Method 2: classify tweets using NLP library and report accuracy.
     *
     */
    public static void classifyTweets() {
        int countCorrect = 0;
        int countWrong = 0;

        if (HANDLER.getTweetBuffer().isEmpty()) {
            System.out.println("Tweet buffer is empty. Nothing to classify. Returning to main menu.");
            return;
        }

        for (AbstractTweet t : HANDLER.getTweetBuffer()) {
            t.setPredictedPolarity(HANDLER.classifyTweet(t));
            if (t.getPredictedPolarity() == t.getTarget()) {
                countCorrect++;
            } else {
                countWrong++;
            }
        }

        System.out.println("Correct classified tweets: " + countCorrect);
        System.out.println("Incorrect classified tweets: " + countWrong);
        double correctRate = (double)countCorrect / ((double)countWrong + (double)countCorrect) * 100;
        System.out.println("Correct prediction rate: " + String.format("%.2f", correctRate) + "%");

    }

    /**
     * Method 3: manually change tweet class label.
     *
     */
    public static void changeTweet() {

        System.out.println("Please input the ID of tweet.");
        int targetId = Integer.parseInt(MainApp.CONSOLEINPUT.nextLine());

        int targetIndex = -1;
        for (AbstractTweet t : HANDLER.getTweetBuffer()) {
            if (t.getId() == targetId) {
                targetIndex = HANDLER.getTweetBuffer().indexOf(t);
            }
        }

        if (targetIndex == -1) {
            System.out.println ("Tweet not found.");
            return;
        } else {
            System.out.println("Current predicted polarity: " + HANDLER.getTweetBuffer().get(targetIndex).getPredictedPolarity());
            System.out.println("Please input the polarity. Must be 0, 2 or 4.");
            int newPolarity = Integer.parseInt(MainApp.CONSOLEINPUT.nextLine());
            if (newPolarity != 0 && newPolarity != 2 && newPolarity != 4) {
                System.out.println("Invalid input. Data will not be changed.");
                return;
            }
            HANDLER.getTweetBuffer().get(targetIndex).setPredictedPolarity(newPolarity);
            System.out.println("Predicted polarity updated.");

        }
    }

    /**
     * Method 4: add new tweets to database.
     *
     */
    public static void addTweets() {
        // Add all tweets in memory into database list
        HANDLER.addTweetsToDB(HANDLER.getTweetBuffer());
        System.out.println("Tweet buffer is now empty.");

        String menu = "\nDo you want to save the database to your file?\n\n"
                + "\t 1. yes.\n"
                + "\t 2. no.\n";

        System.out.println(menu);
        String selection = MainApp.CONSOLEINPUT.nextLine();

        switch (selection) {
            case "1":
                // 1. save.
                HANDLER.saveSerialDB();
                break;
            default:
                // Others. do not save.
                System.out.println("Data will not be saved to the file now.");
                break;
        }
    }

    /**
     * Method 5: delete tweet from database (given its id).
     *
     */
    public static void deleteTweet() {

        System.out.println("Please input the ID of tweet.");
        int inputId = Integer.parseInt(MainApp.CONSOLEINPUT.nextLine());

        HANDLER.deleteTweet(inputId);

    }

    /**
     * Method 6: search tweets by user, date, flag, or a matching substring.
     @return list of tweets that match the search
     */
    public static List<AbstractTweet> searchTweet() {
        List<AbstractTweet> resultList = new ArrayList<AbstractTweet>();

        String menu = "\nThis function will search both data list. Choose one of the following functions:\n\n"
                + "\t 1. Search by user.\n"
                + "\t 2. Search by date.\n"
                + "\t 3. Search by flag.\n"
                + "\t 4. Search by substring\n";
        System.out.println(menu);
        String selection = MainApp.CONSOLEINPUT.nextLine();
        String inputInfo;
        switch (selection) {
            case "1":
                // 1. search by user.
                System.out.println("Please input the user name.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                resultList = HANDLER.searchByUser(inputInfo);
                break;
            case "2":
                // 2. search by date.
                System.out.println("Please input the date.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                try {
                    Date dateInput = formatter.parse(inputInfo);
                    resultList = HANDLER.searchByDate(dateInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                // 3. search by flag.
                System.out.println("Please input the flag.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                resultList = HANDLER.searchByFlag(inputInfo);
                break;
            case "4":
                // 4. search by substring.
                System.out.println("Please input the substring.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                resultList = HANDLER.searchBySubstring(inputInfo);
                break;
            default:
                System.out.println("That is not a recognized command.");
                break;
        }

        return resultList;
    }

    /**
     * Print out the formatted table for list
     @param target_List
     */
    public static void printList(List<AbstractTweet> target_List) {
        String line = "----------------------------------------------------------------------------------"
                + "----------------------------------------------------------------------------------";
        String information = "| ";
        information = information + String.format("%6s", "Target") + " | ";
        information = information + String.format("%6s", "Class") + " | ";
        information = information + String.format("%6s", "ID") + " | ";
        information = information + String.format("%30s", "Date") + " | ";
        information = information + String.format("%10s", "Flag") + " | ";
        information = information + String.format("%15s", "User") + " | ";
        information = information + String.format("%70s", "Text") + " |";

        System.out.println(line);
        System.out.println(information);
        System.out.println(line);
        for (AbstractTweet t : target_List) {
            System.out.println(t);
            System.out.println(line);
        }
    }
}
