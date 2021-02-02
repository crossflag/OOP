/**
 * @Author: Diego Sebastian Santana
 * This is the main class for all primary test cases on the TweetDatabase!
 * Only two of these cases should intentionally fail the rest should function as Intended.
 */

package sentimentanalysis;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import static org.junit.Assert.*;

public class TweetHandlerTest {
    /**
     * Global initializers for all test objects.
     */
    private static TweetHandler handler;
    private static List<AbstractTweet> Expected;
    private static List<AbstractTweet> Actual;
    private static List<AbstractTweet> temp;
    private static SimpleDateFormat formatter;
    private static Date date1;
    private static Date date2;
    private static Date date3;
    private static AbstractTweet tweet1;
    private static AbstractTweet tweet2;
    private static AbstractTweet tweet3;

    /**
     * Initializes all the objects before all cases
     */
    @BeforeClass
    public static void BeginTest(){
        handler = new TweetHandler();
        formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try{
            date1 = formatter.parse("Thu May 10 21:15:34 UTC 2018");
            date2 = formatter.parse("Mon Jun 11 22:05:12 UTC 2018");
            date3 = formatter.parse("Fri Jul 27 07:05:12 UTC 2018");
        }catch (ParseException e) {
            e.printStackTrace();
        }
        tweet1 = new Tweet(0, 2, date1 , "Tester1" , "TestUSER1",
                "HELLO! and goodmorning this is a regularly scheduled test!");
        tweet2 = new Tweet(4, 4, date2, "Tester2", "TestUSER2",
                "The second test has now commenced. GREETINGS!");
        tweet3 = new Tweet(2, 6, date3, "Tester3", "TestUSER3",
                "It is time for a third test. Are you ready?");

    }

    /**
     * primes every ArrayList before each test
     */
    @Before
    public void initializer(){
        Expected = new ArrayList<AbstractTweet>();
        Actual = new ArrayList<AbstractTweet>();
        temp = new ArrayList<AbstractTweet>();
    }

    /**
     * Wipes ArrayList after every test
     */
    @After
    public void ClearTest(){
        Expected = null;
        Actual = null;
        temp = null;
    }

    /**
     * Tests if Load tweets from text pulls the functions from a designated test file
     * NOTE: Test file is included
     * NOTE: FILE PATH MUST BE CHANGED DURING GRADING
     */
    @org.junit.Test
    public void testLoadTweetsFromText() {
        Expected.add(tweet1);
        Expected.add(tweet2);
        Expected.add(tweet3);
        Actual = handler.loadTweetsFromText("C:\\Users\\spela\\Desktop\\OBJORI\\Assign3\\dsp50_JUnit-master\\SentimentAnalysis\\test.csv");
        assertEquals(Expected, Actual);
    }

    /**
     * Tests if the parser correctly reads in each part of the tweet as its intended data type.
     */
    @org.junit.Test
    public void testParseTweetLine() {
        String input = "\"0\",\"2\",\"Thu May 10 21:15:34 UTC 2018\",\"Tester\",\"Test\",\"Hello and goodmorning this is a regularly scheduled test!\"";
        Tweet Actual = handler.parseTweetLine(input);
        assertEquals(0, Actual.getTarget());
        assertEquals(2, Actual.getId());
        assertEquals(date1, Actual.getDate());
        assertEquals("Tester",Actual.getFlag());
        assertEquals("Test",Actual.getUser());
        assertEquals("Hello and goodmorning this is a regularly scheduled test!", Actual.getText());
    }

    /**
     * Tests what happens if an unexpected data type is put in the tweet
     * NOTE: THIS METHOD IS BUILT TO FAIL. IT SHOULD NOT PASS
     */
    @org.junit.Test
    public void testUnexpectedDataInput(){
        String input =   "\"10\",\"2\",\"Mon May 11 02:15:34 UTC 2018\",\"Tester1\",\"Test1\",\"HELLO! and goodmorning this is a regularly scheduled test!\"";
        Tweet Actual = handler.parseTweetLine(input);
        assertFalse("ERROR: Number out of conditional bounds!", tweet1.getTarget()!=Actual.getTarget());
    }

    /**
     * Tests the date parser if the date itself is missing the day
     * @throws Exception
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testDateParse()throws Exception{
        String input = "\"0\",\"2\",\"may 11 02:15:34 utc 2018\",\"Tester1\",\"Test1\",\"HELLO! and goodmorning this is a regularly scheduled test!\"";
        Tweet ATweet = handler.parseTweetLine(input);
        assertEquals(tweet1, ATweet);
    }

    /**
     * Tests if the parse tweet line will accept tweets in a completely different order.
     * NOTE: Primarily reversed
     */
    @org.junit.Test(expected = NumberFormatException.class)
    public void testTweetReversal(){
        String reverseInput = "\"It is time for a third test. Are you ready?\",\"Test3\",\"Tester3\",\"Mon July 27 12:05:12 UTC 2018\",\"6\", \"2\"";
        Tweet REVERSAL = handler.parseTweetLine(reverseInput);
        assertEquals(tweet3, REVERSAL);
    }

    /**
     * Tests the tweet classifier method
     */
    @org.junit.Test
    public void testClassifyTweet() {
        int Actual = handler.classifyTweet(tweet1);
        assertEquals(0, Actual);
    }

    /**
     * Tests the to see if tweets are actually pushed into the data base.
     */
    @org.junit.Test
    public void testAddTweetsToDB() {
        Expected.add(tweet1);
        Actual.add(tweet1);
        handler.addTweetsToDB(Expected);
        int expectedcount = Expected.size();
        int actualcount = Actual.size();
        assertEquals(expectedcount, actualcount);
    }

    /**
     * Tests to see if it will truly recognize duplicate tweets.
     */
    @org.junit.Test
    public void testCheckAddDupes(){
        Expected.add(tweet1);
        Expected.add(tweet3);
        Expected.add(tweet3);
        handler.addTweetsToDB(Expected);
        assertSame(handler.getTweetDB().get(1),Expected.get(2));
    }

    /**
     * Tests to see if you can add an empty ArrayList to the database!
     * NOTE: THIS METHOD IS BUILT TO FAIL IT SHOULD NOT PASS
     */
    @org.junit.Test
    public void testAddEmpty(){
        handler.addTweetsToDB(Actual);
        assertFalse("Cannot add an empty Array baka!", Actual.size() != handler.getTweetDB().size());
    }

    /**
     * Tests to see if the delete method works
     */
    @org.junit.Test
    public void testDeleteTweet() {
        Expected.add(tweet1);
        handler.deleteTweet(tweet1.getId());
        assertEquals(0, Actual.size());
    }

    /**
     * Tests to see if the delete method will work with target input instead of ID
     */
    @org.junit.Test
    public void testTargetDelete(){
        Expected.add(tweet1);
        Expected.add(tweet2);
        Expected.add(tweet3);
        handler.addTweetsToDB(Expected);
        handler.deleteTweet(tweet2.getId());
        Actual = handler.getTweetDB();
        assertEquals(2, handler.getTweetDB().size());
    }

    /**
     * Tests the user search method
     */
    @org.junit.Test
    public void testSearchByUser() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchByUser(tweet1.getUser());
        assertEquals(Expected, Actual);
    }

    /**
     * Tests if user search method with case sensitivity
     */
    @org.junit.Test
    public void TestLowercaseUserSearch(){
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchByUser("testuser1");
        assertEquals(Expected,Actual);
    }

    /**
     * Tests user search method with case sensitivity
     */
    @org.junit.Test
    public void TestUppercaseUserSearch() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchByUser("TESTUSER1");
        assertEquals(Expected, Actual);
    }

    /**
     * Tests the date search method
     */
    @org.junit.Test
    public void testSearchByDate() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchByDate(tweet1.getDate());
        assertEquals(Expected, Actual);
    }

    /**
     * Tests if date method will function with only a portion of the date.
     * @throws Exception
     */
    @org.junit.Test(expected = ParseException.class)
    public void testIncompleteDateSearch() throws Exception{
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        System.out.println("INCORRECT DATE FORMAT!!!");
        Date testDate = formatter.parse("May 10 21:15:34 2018");
        temp = handler.searchByDate(testDate);
        assertEquals(date1, temp);
    }

    /**
     * Tests the flag search method
     */
    @org.junit.Test
    public void testSearchByFlag() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        temp = handler.searchByFlag(tweet1.getFlag());
        assertEquals(Expected, temp);
    }

    /**
     * Tests the flag search method with case sensitivity
     */
    @org.junit.Test
    public void TestLowercaseFlagSearch(){
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchByFlag("tester1");
        assertEquals(Expected,Actual);
    }

    /**
     * Tests the flag search method with case sensitivity
     */
    @org.junit.Test
    public void TestUppercaseFlagSearch() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchByFlag("TESTER1");
        assertEquals(Expected, Actual);
    }

    /**
     * Tests the substring search method
     */
    @org.junit.Test
    public void searchBySubstring() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchBySubstring(tweet1.getText());
        assertEquals(Expected, Actual);
    }

    /**
     * Tests the substring search method with case sensitivity
     */
    @org.junit.Test
    public void TestLowercaseSubStringSearch(){
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchBySubstring("hello");
        assertEquals(Expected,Actual);
    }

    /**
     * Tests the substring search method case sensitivity
     */
    @org.junit.Test
    public void TestUppercaseSubStringSearch() {
        Expected.add(tweet1);
        handler.addTweetsToDB(Expected);
        Actual = handler.searchBySubstring("GOODMORNING");
        assertEquals(Expected, Actual);
    }
}