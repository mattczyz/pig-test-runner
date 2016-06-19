package pig.testing.validation;

import java.util.Properties;

public interface ResultValidator {
    void setArgs(Properties args);
    void validate() throws AssertionError;
}
