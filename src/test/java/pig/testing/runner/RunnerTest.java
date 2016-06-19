package pig.testing.runner;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pig.testing.exec.ParameterizedTests;

@RunWith(Suite.class)
@SuiteClasses( {
  ParameterizedTests.class
} )
public class RunnerTest {
    @BeforeClass
    public static void init() {
        if(System.getProperty("stopAtInit") != null){
            ParameterizedTests.initOnly = true;
        }
    }
}
