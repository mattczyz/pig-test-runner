package pig.testing.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;

public class PigExecutor implements AppExecutor{

    public void execScript(String file, Properties props) throws IOException, ParseException {

        PigTest test = new PigTest(file, propsToArray(props));
        test.unoverride("STORE");
        test.unoverride("DUMP");
        test.runScript();

    }

    private static String[] propsToArray(Properties props) {
        List<String> keyValuePairs = new ArrayList<String>();

        for (Entry<Object, Object> entry : props.entrySet()) {
            keyValuePairs.add(entry.getKey() + "=" + entry.getValue());
        }

        return keyValuePairs.toArray(new String[keyValuePairs.size()]);
    }
}
