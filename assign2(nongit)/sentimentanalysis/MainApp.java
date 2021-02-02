package sentimentanalysis;
import java.util.*;

public class MainApp {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        //TweetDatabase /* not sure what here */ = new TweetDatabase();

        String welcomeString = "\n Welcome to Twitter Reader LLC, Please choose one of the following: \n\n"
           +"\t0. Exit program.\n"
           +"\t1. Load new tweet text file.\n"
           +"\t2. Classify tweets using NLP library.\n"
           +"\t3. Manually change tweet class label.\n"
           +"\t4. Add new tweets to database.\n"
           +"\t5. Delete tweet from database (given its id).\n"
           +"\t7. Search tweets by user, date, flag, or a matching substring.\n";
        String changeLabel = "Please provide a new class label: \n";
        String iDdelete = "Please provide an ID number: \n";

    }
}
