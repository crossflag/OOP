package sentimentanalysis;

import java.util.Date;

public class tweetDecorator extends Tweet {
    /**
     * Constructor from csv file input: uses data fields use them to set the parameters.
     *
     * @param target ground truth sentiment label
     * @param id     tweetDBID
     * @param date   date the tweet was posted
     * @param flag   tweet flag
     * @param user   userID
     * @param text   Tweet text
     */
    public tweetDecorator(int target, int id, Date date, String flag, String user, String text) {
        super(target, id, date, flag, user, text);
    }

}
