package sentimentanalysis;

import java.util.Date;
import java.util.IdentityHashMap;

public class Tweet extends AbstractTweet {

   public Tweet(int target, int id, Date date, String flag, String User, String text){
        this.id = id;
        this.target = target;
        this.date = date;
        this.flag = flag;
        this.User = User;
        this.text = text;
        this.predictedPolarity = 2;
    }


    private final int id;
    private final int target;
    private final Date date;
    private final String flag;
    private final String User;
    private final String text;
    private int predictedPolarity;

    public int getTarget() {
        return target;
    }

    /**
     *
     * @return
     */
    public int getiD() {
        return id;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return
     */
    public String getFlag() {
        return flag;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return User;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @return
     */
    public int getPredictedPolarity() {
        return predictedPolarity;
    }

    /**
     *
     * @param predictedPolarity
     */
    public void setPredictedPolarity(int predictedPolarity) {
        this.predictedPolarity = predictedPolarity;
    }

}
