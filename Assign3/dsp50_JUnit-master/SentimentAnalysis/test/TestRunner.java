/**
 * @Author: Diego Sebastian Santana
 * This is the primary main test runner that checks and displays the completed number of cases that ran as intended
 * If functioning correctly should return two cases that do not function.
 */
package sentimentanalysis;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args){
        System.out.println("INITIALIZING THE TESTS");
        System.out.println("===========================================");
        Result result = JUnitCore.runClasses(TweetHandlerTest.class);
        System.out.println("===========================================");
        if (result.getFailures().size() == 0){
            System.out.println("All tests successful!");
        }else{
            System.out.println("Number of failed test cases= "+result.getFailures().size());
            for(Failure failure : result.getFailures())
                System.out.println(failure.toString());
        }
    }
}
