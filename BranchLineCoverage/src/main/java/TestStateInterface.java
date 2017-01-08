/**
 * Created by tolgacaner on 24/11/16.
 */
public interface TestStateInterface {

    void testStarted(String functionName);
    void testFinished(String functionName);
    void allTestsFinished();
}
