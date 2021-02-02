package sentimentanalysis;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.*;


public class TweetDatabase {
    private ArrayList<AbstractTweet> twitterList;

    private void showTweets(ArrayList<AbstractTweet> tlist){
        for (AbstractTweet t : tlist){
            System.out.println(" ------------------------------------------------------------------------------ ");
            //System.out.printf(t.getTarget(), t.getId(), t.getDate(), t.getFlag(),
            //t.getUser(), t.getText(), t.getPredictedPolarity());
            System.out.println(" ------------------------------------------------------------------------------ ");
        }
    }
    public TweetDatabase() throws IOException {
        twitterList = new ArrayList<>();

        try{
            FileInputStream fis = new FileInputStream("testdata.manual.2009.06.14.csv");
            ObjectInputStream ois = new ObjectInputStream(fis);
            twitterList = (ArrayList<AbstractTweet>) ois.readObject();

            fis.close();
        }
        catch(FileNotFoundException fnfe){
            System.out.println("File does not exist, Creating one...");
            FileOutputStream fos = new FileOutputStream("testdata.manual.2009.06.14.csv");
            fos.close();
        }catch(ClassNotFoundException e){
            System.out.print(e);
            e.printStackTrace();
        }catch (IOException e){
            System.out.print(e);
            e.printStackTrace();
        }
    }
    public void showTweets(){
        showTweets(twitterList);
    }
    public void flush() throws IOException{
        FileOutputStream fos = new FileOutputStream("testdata.manual.2009.06.14.csv");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(twitterList);
        fos.close();
    }
}
