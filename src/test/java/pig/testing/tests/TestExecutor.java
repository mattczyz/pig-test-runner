package pig.testing.tests;

import java.util.Properties;

public interface TestExecutor {
    void setArgs(Properties args);
    void execute() throws AssertionError;
}
