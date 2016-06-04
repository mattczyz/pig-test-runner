package pig.testing.runner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import pig.testing.runner.PigTestDef.TestClass;
import pig.testing.tests.TestExecutor;
import pig.testing.tests.TestExecutorFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class PigScriptsTests {
    @Parameters(name = "id = {0}")
    public static Collection<Object[]> data() throws JsonParseException,
            JsonMappingException, IOException, HiveException {
        String pigTestDefs = System.getProperty("pigTestDefs");

        String jsonInput = new String(Files.readAllBytes(Paths
                .get(pigTestDefs)));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode testDefsNode = mapper.readTree(jsonInput);

        Iterator<JsonNode> testDefs = testDefsNode.iterator();
        ArrayList<Object[]> testProperties = new ArrayList<Object[]>();
        while(testDefs.hasNext()){
            PigTestDef properties = mapper.readValue(testDefs.next(), PigTestDef.class);

            testProperties.add(new Object[] { properties.getId(),
                    properties.getFile(), properties.getArgs(),
                    properties.getTests(), properties.getHiveCli() });

        }

        return testProperties;
    }

    private String id;
    private String file;
    private Properties args;
    private ArrayList<TestClass> tests;
    private ArrayList<String[]> hiveCli;
    
    public PigScriptsTests(String id, String file, Properties args,
            ArrayList<TestClass> tests, ArrayList<String[]> hiveCli) {

        this.id = id;
        this.file = file;
        this.args = args;
        this.tests = tests;
        this.hiveCli = hiveCli;
    }
    
    @Test
    public void initialize() {
        if(System.getProperty("stopAtInit") != null)
            System.exit(0);
    }
    
    @Test
    public void test() throws IOException, ParseException, URISyntaxException, HiveException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // exceute hive cli
        for(String[] hiveCliArgs:hiveCli){
            HiveExecutor.execHive(hiveCliArgs);
        }
        
        // execute pig
        execPigScript(this.id, this.file, this.args);

        
        // test with expected
        for (TestClass test : tests) {
            TestExecutor testExecutor = TestExecutorFactory.get(test.getName());
            
            testExecutor.setArgs(test.getArgs());
            testExecutor.execute();

        }
    }

    private void execPigScript(String id, String file, Properties props)
            throws IOException, ParseException {

        PigTest test = new PigTest(file, propsToArray(props));
        test.unoverride("STORE");
        test.unoverride("DUMP");
        test.runScript();

    }

    private String[] propsToArray(Properties props) {
        List<String> keyValuePairs = new ArrayList<String>();

        for (Entry<Object, Object> entry : props.entrySet()) {
            keyValuePairs.add(entry.getKey() + "=" + entry.getValue());
        }

        return keyValuePairs.toArray(new String[keyValuePairs.size()]);
    }
}
