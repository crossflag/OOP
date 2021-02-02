/**
 * @Author Sebastian Santana , Dalton Melville
 *
 * */
package sentimentanalysis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.DimensionUIResource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.*;
import java.util.logging.Logger;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;


public class TwitterGUI {

    TwitterGUI(){

    }
    private static final Logger logger = Logger.getLogger(TwitterGUI.class.getName());
    private static FileHandler fh;

    // inner class to handle button events
    private static class ButtonListener implements ActionListener {
        private int nEvents = 0;
        private static TweetHandler handler = new TweetHandler();
        private static MainApp mhand = new MainApp();
        private static List<AbstractTweet> tweets = new ArrayList<>();
        private static List<AbstractTweet> searchRez = new ArrayList<>();
        private static List<AbstractTweet> dataB = new ArrayList<>();




        /**
         * Respond to events generated by the button by printing the
         * command and number of times the event has been triggered.
         *
         * @param e the event created by the button when it was clicked.
         */
        public void actionPerformed(ActionEvent e) {
            nEvents++;
            System.out.println(e.getActionCommand() + " " + nEvents);
        }

        /**
         *
         * @param args
         * @throws IOException
         * Main: Performs all of the actionPerformed functions that allow the buttons to function the way they should.
         */
        public static void main(String[] args) throws IOException{
            fh = new FileHandler("log.txt");
            logger.addHandler(fh);

            Border blackline;
            blackline = BorderFactory.createLineBorder(Color.black);

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JPanel panel = new JPanel(new BorderLayout());
            JPanel panel2 = new JPanel(new BorderLayout());
            JTextArea viz = new JTextArea();
            viz.setPreferredSize(new Dimension(1880,240));
            viz.setBorder(blackline);
            frame.add(panel);
            panel.add(panel2);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(720,560));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation((screen.width - 400)/2,(screen.height-400)/2 );
            frame.add(panel);

            JRadioButton user = new JRadioButton("USER");
            JRadioButton date = new JRadioButton("DATE");
            JRadioButton flag = new JRadioButton("FLAG");
            JRadioButton subStr = new JRadioButton("SUBSTRING");
            ButtonGroup group = new ButtonGroup();

            handler.loadSerialDB();
            dataB = handler.getTweetDB();

            JButton add = new JButton("Add to DB");

            add.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e
                 * Adds the tweets to the Data Base.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.log(Level.INFO, "User has pressed the addtoDB Button");
                    int ignore = 0;
                    int count = 0;
                    handler.addTweetsToDB(tweets);
                    dataB = handler.getTweetDB();
                    if(dataB.isEmpty()){
                        viz.append("\n ERROR DID NOT IMPORT CORRECTLY TO DATABASE!");
                        logger.log(Level.WARNING, "NOTHING FOUND. NO TWEETS WERE IMPORTED");
                    }else{
                        handler.saveSerialDB();

                        for(AbstractTweet tweet: tweets){
                            if(dataB.contains(tweet)){
                                ignore++;
                            }else{
                                count++;
                            }
                        }
                        viz.append("\n Import Successful");
                        viz.append("\n Number of tweets ignores: " + ignore);
                        viz.append("\n Number of new tweets imported: " + count);
                    }
                }
            });
            JButton save = new JButton("Save to DB");
            save.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e
                 * Will save the tweets to the Data Base
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.log(Level.INFO, "User has pressed the saveToDB Button");
                    handler.saveSerialDB();
                    viz.append("\n System Saved!");
                }
            });
            JButton classify = new JButton("Classify Tweets");
            classify.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e
                 * Classify the Tweets based on their polarities.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.log(Level.INFO, "User has pressed the Classify Tweets Button");
                    handler.finalClassify();
                    if(dataB.isEmpty()){
                        logger.log(Level.WARNING, "NOTHING FOUND, NO TWEETS TO CLASSIFY.");
                        viz.append("\n CANT CLASSIFY NOTHING?");
                    }else{
                        logger.log(Level.INFO, "Congrats it classified");
                        viz.append("\n CLASSIFY SUCCESS, TOTAL READ IN " + handler.getTweetBuffer().size());
                        classify.updateUI();
                    }
                }
            });
            JButton display = new JButton("Display Tweets!");
            display.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e
                 * Displays the current arraylist of tweets.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.log(Level.INFO, "User has chosen to Display tweets");
                    dataB = handler.getTweetDB();
                    doJtab(dataB);
                }
            });
            JButton flush = new JButton("Flush System");
            flush.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e
                 * Will completely remove all of the tweets in the Data Base.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    logger.log(Level.INFO,"System Flushed by User.");
                    while(!dataB.isEmpty())
                    {
                        for(int i = 0; i < dataB.size(); i++)
                            dataB.remove(i);
                    }
//                        int id = Integer.parseInt(t.getText());
//                        handler.deleteTweet(id);

                    handler.saveSerialDB();
                    viz.append("\n System Flushed!");
                }
            });
            String[] choices = { "Load", "Delete","Search", "Change Tweet", "Exit"};
            JComboBox menu = new JComboBox(choices);

            menu.addActionListener(new ActionListener() {
                /**
                 *
                 * @param e
                 * Creates the dropdown menu for the GUI in which a user can load, delete, search, change tweet or exit
                 * the program.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {

                    String choices = (String)menu.getSelectedItem();
                    switch (choices){
                        case "Load":
                            logger.log(Level.INFO, "User has loaded tweets to DB");
                            JFrame frame = new JFrame("LOADIN");
                            JPanel panel = new JPanel(new FlowLayout());
                            panel.setPreferredSize(new Dimension(200,50));
                            JTextField path = new JTextField();
                            path.setPreferredSize(new Dimension(100,40));
                            JButton ok = new JButton("OK");
                            ok.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String x = path.getText();
                                    logger.log(Level.INFO, "Tweets are importing");
                                    viz.append("\n TWEETS IMPORTING...");
                                    tweets = handler.loadTweetsFromText(x);
                                    if(tweets.isEmpty()){
                                        logger.log(Level.WARNING, "NOTHING FOUND, LOADING TWEETS FAILED.");
                                        viz.append("ERROR DID NOT LOAD");
                                    }else{
                                        logger.log(Level.INFO, "User has loaded tweets");
                                        viz.append("\n Tweets Loaded!");
                                    }
                                }
                            });
                            frame.add(panel);
                            panel.add(path);
                            panel.add(ok);
                            panel.setVisible(true);
                            frame.pack();
                            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                            frame.setLocation((screen.width - 400)/2,(screen.height-400)/2 );
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setVisible(true);
                            break;


                        case "Delete":
                            logger.log(Level.INFO, "User has chosen to delete tweets");
                            frame = new JFrame("DELETIN");
                            panel = new JPanel(new FlowLayout());
                            JLabel idnum = new JLabel("Enter ID#: ");
                            JTextField search = new JTextField();
                            search.setPreferredSize(new Dimension(100, 25));
                            ok = new JButton("OK");

                            frame.add(panel);
                            panel.add(idnum);
                            panel.add(search);
                            panel.add(ok);
                            panel.updateUI();

                            ok.addActionListener(new ActionListener() {
                                /**
                                 *
                                 * @param e
                                 * once "OK" is pressed the tweets will be deleted based on ID
                                 */
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    logger.log(Level.INFO, "User has deleted a tweet");
                                    int id = Integer.parseInt(search.getText());
                                    if(!handler.deleteTweet(id)){
                                        logger.log(Level.WARNING, "NOTHING FOUND. NO TWEETS TO DELETE");
                                        viz.append("\n ERROR NO TWEET!");
                                    }else {
                                        logger.log(Level.INFO, "User has successfully deleted tweet");
                                        viz.append("\n Tweet Deleted");
                                    }
                                }
                            });

                            screen = Toolkit.getDefaultToolkit().getScreenSize();
                            frame.setLocation((screen.width - 400)/2,(screen.height-400)/2 );
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setVisible(true);
                            frame.pack();
                            break;

                        case "Search":

                            frame = new JFrame("SEARCHIN");
                            panel = new JPanel(new FlowLayout());
                            JTextField searcher = new JTextField();
                            searcher.setPreferredSize(new Dimension(100, 40));
                            ok = new JButton("OK");
                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

                            group.add(user);
                            group.add(date);
                            group.add(flag);
                            group.add(subStr);

                            user.setActionCommand("USER");
                            user.setSelected(true);
                            date.setActionCommand("DATE");
                            flag.setActionCommand("FLAG");
                            subStr.setActionCommand("SUBSTRING");

                            panel.add(user);
                            panel.add(date);
                            panel.add(flag);
                            panel.add(subStr);
                            panel.add(searcher);
                            panel.add(ok);

                            panel.updateUI();

                            ok.addActionListener(new ActionListener() {
                                /**
                                 *
                                 * @param e
                                 * Allows the user to search by user, flag, substring, or date.
                                 */
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    logger.log(Level.INFO, "User has chosen to search by " + searchRez);
                                    String gogo = searcher.getText();

                                    if(user.isSelected()){
                                        logger.log(Level.INFO, "User has chosen to search by User.");
                                        searchRez = handler.searchByUser(gogo);
                                        if(searchRez.isEmpty()){
                                            logger.log(Level.WARNING, "NOTHING FOUND. NO USER" + gogo + " FOUND");
                                            viz.append("\n FOUND NOTHING");
                                        }else{
                                            doJtab(searchRez);
                                        }
                                    }
                                    else if(flag.isSelected()){
                                        logger.log(Level.INFO, "User has chosen to search by Flag.");
                                        searchRez = handler.searchByFlag(gogo);
                                        if(searchRez.isEmpty()){
                                            logger.log(Level.WARNING, "NOTHING FOUND. NO FLAG " + gogo + " FOUND");
                                            viz.append("\n FOUND NOTHING");
                                        }else{
                                            doJtab(searchRez);
                                        }
                                    }
                                    else if(date.isSelected()){
                                        logger.log(Level.INFO, "User has chosen to search by Date.");
                                        try {
                                            searchRez = handler.searchByDate(formatter.parse(gogo));
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                        if(searchRez.isEmpty()){
                                            logger.log(Level.WARNING, "NOTHING FOUND. NO DATE " + gogo + " FOUND");
                                            viz.append("\n FOUND NOTHING");
                                        }else{
                                            doJtab(searchRez);
                                        }
                                    }
                                    else if(subStr.isSelected()){
                                        logger.log(Level.INFO, "User has chosen to search by Substring.");
                                        searchRez = handler.searchBySubstring(gogo);
                                        if(searchRez.isEmpty()){
                                            logger.log(Level.WARNING, "NOTHING FOUND. NO SUBSTRING " + gogo + " FOUND");
                                            viz.append("\n FOUND NOTHING");
                                        }else{
                                            doJtab(searchRez);
                                        }
                                    }

                                }
                            });
                            frame.add(panel);
                            panel.setVisible(true);
                            frame.pack();
                            screen = Toolkit.getDefaultToolkit().getScreenSize();
                            frame.setLocation((screen.width - 400)/2,(screen.height-400)/2 );
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setVisible(true);
                            break;

                        case "Change Tweet":

                            logger.log(Level.INFO, "User has chosen to change tweets");
                            frame = new JFrame("CHANGIN");
                            panel = new JPanel(new FlowLayout());
                            idnum = new JLabel("Enter ID#: ");
                            search = new JTextField();
                            search.setPreferredSize(new Dimension(100, 25));
                            ok = new JButton("OK");

                            frame.add(panel);
                            panel.add(idnum);
                            panel.add(search);
                            panel.add(ok);
                            panel.updateUI();

                            ok.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    String query = search.getText();
                                    int idFind = Integer.parseInt(query);
                                    for(AbstractTweet t: dataB)
                                    {
                                        if(idFind == t.getId()){
                                            JFrame polch = new JFrame("Polarity");
                                            JPanel polcha = new JPanel(new FlowLayout());
                                            JLabel changer = new JLabel("Enter Desired Polarity: ");
                                            JTextField polar = new JTextField();
                                            polar.setPreferredSize(new Dimension(100,25));

                                            JButton ok = new JButton("OK");
                                            polch.add(polcha);
                                            polcha.add(changer);
                                            polcha.add(polar);
                                            polcha.add(ok);
                                            polch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                            polch.setVisible(true);
                                            polch.pack();
                                            polcha.updateUI();

                                            ok.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                  String nClass = polar.getText();
                                                  int nIntC =Integer.parseInt(nClass);
                                                  t.setPredictedPolarity(nIntC);
                                                  handler.saveSerialDB();
                                                  viz.append("Tweet of " + t.getId() + "\n");
                                                  viz.append("Class of tweet changed to " + nIntC + "\n");
                                                }
                                            });
                                    }
                                    }

                                }
                            });
                            screen = Toolkit.getDefaultToolkit().getScreenSize();
                            frame.setLocation((screen.width - 400)/2,(screen.height-400)/2 );
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.setVisible(true);
                            frame.pack();
                            break;


                        case "Exit":
                            System.exit(0);
                            break;
                    }
                }
            });

            menu.setVisible(true);
            panel.add(menu, BorderLayout.PAGE_START);
            viz.setVisible(true);
            panel.add(viz,BorderLayout.PAGE_END);
            panel.add(save,BorderLayout.EAST);
            panel.add(add, BorderLayout.WEST);
            panel2.add(display, BorderLayout.WEST);
            panel2.add(classify, BorderLayout.EAST);
            panel2.add(flush, BorderLayout.CENTER);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    /**
     *
     * @param searchRez
     * Will display the arraylist of tweets in a J Table.
     */
    private static void doJtab(List<AbstractTweet> searchRez){
        JFrame searches = new JFrame("Search Results");

        String[] colTypes = {"Target", "ID", "Date", "Flag","User", "Text"};
        String[][] data = new String[searchRez.size()][6];

        for(int i = 0; i<searchRez.size(); i++){
            data[i][0] = Integer.toString(searchRez.get(i).getTarget());
            data[i][1] = Integer.toString(searchRez.get(i).getId());
            data[i][2] = String.valueOf(searchRez.get(i).getDate());
            data[i][3] = searchRez.get(i).getUser();
            data[i][4] = searchRez.get(i).getFlag();
            data[i][5] = searchRez.get(i).getText();
        }

        JTable table = new JTable(data, colTypes);
        JScrollPane scrollPane = new JScrollPane(table);

        searches.add(scrollPane,BorderLayout.PAGE_START);
        searches.setVisible(true);
        searches.pack();
        searches.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

}
