package pig.testing.exec;

import java.util.Properties;

public interface AppExecutor {

    public void execScript(String file, Properties args) throws Exception;

}