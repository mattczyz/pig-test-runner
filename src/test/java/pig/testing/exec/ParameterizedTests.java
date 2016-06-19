package pig.testing.exec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.hadoop.hive.ql.metadata.HiveException;
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

import pig.testing.exec.TestDef.TestClass;
import pig.testing.validation.ResultValidator;
import pig.testing.validation.ResultValidatorFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class ParameterizedTests {
    @Parameters(name = "id = {0}")
    public static Collection<Object[]> data() throws JsonParseException,
            JsonMappingException, IOException, HiveException {
        String testDefsPath = System.getProperty("testDefs");

        String jsonInput = new String(Files.readAllBytes(Paths
                .get(testDefsPath)));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode testDefsNode = mapper.readTree(jsonInput);

        Iterator<JsonNode> testDefs = testDefsNode.iterator();
        ArrayList<Object[]> testProperties = new ArrayList<Object[]>();
        while(testDefs.hasNext()){
            TestDef properties = mapper.readValue(testDefs.next(), TestDef.class);

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
    
    public static boolean initOnly = false;
    
    public ParameterizedTests(String id, String file, Properties args,
            ArrayList<TestClass> tests, ArrayList<String[]> hiveCli) {

        this.id = id;
        this.file = file;
        this.args = args;
        this.tests = tests;
        this.hiveCli = hiveCli;
    }
        
    @Test
    public void test() throws IOException, ParseException, URISyntaxException, HiveException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        org.junit.Assume.assumeFalse("Initialization only run: " + initOnly, initOnly);
        
        // execute hive cli
        HiveExecutor.cleanup();
        for(String[] hiveCliArgs:hiveCli){
            if(System.getProperty("hive.metastore.uris") == null){
                String[] metastoreArgs = (String[]) ArrayUtils.addAll(hiveCliArgs, HiveExecutor.getMetastoreConfig());
                HiveExecutor.execHive(metastoreArgs);
            } else {
                HiveExecutor.execHive(hiveCliArgs);
            }
        }
        
        // execute pig
        PigExecutor.execPigScript(this.id, this.file, this.args);

        
        // test with expected
        for (TestClass test : tests) {
            ResultValidator testExecutor = ResultValidatorFactory.get(test.getName());
            
            testExecutor.setArgs(test.getArgs());
            testExecutor.validate();

        }
    }
}
