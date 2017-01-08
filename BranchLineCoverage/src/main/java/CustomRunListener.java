import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.regex.Pattern;

/**
 * Created by tolgacaner on 24/11/16.
 */
public class CustomRunListener extends RunListener {

    TestStateInterface testStateDelegate;

    public CustomRunListener(TestStateInterface testStateDelegate) {
        this.testStateDelegate =testStateDelegate;
    }

    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        String[] parts = description.getDisplayName().split(Pattern.quote("("));
        testStateDelegate.testStarted(parts[0]);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        String[] parts = description.getDisplayName().split(Pattern.quote("("));
        testStateDelegate.testFinished(parts[0]);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        testStateDelegate.allTestsFinished();
    }
}
