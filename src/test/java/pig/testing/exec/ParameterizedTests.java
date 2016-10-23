package pig.testing.exec;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.hadoop.hive.ql.metadata.HiveException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
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

            if(properties.getTests().isEmpty()){
                testProperties.add(new Object[] { properties.getId(),
                        properties.getFile(), properties.getArgs(), properties.getHiveCli(), null, properties.getType() });
            } else {
                int i = 0;
                for(TestClass test : properties.getTests()){
                    String id;
                    if(properties.getTests().size() <= 1){
                        id = properties.getId() +  (test.id != null ? " - " + test.id : "");
                    } else {
                        id = properties.getId() + " - " + (test.id == null ? i : test.id);
                            
                    }
                    testProperties.add(new Object[] { id,
                            properties.getFile(), properties.getArgs(), properties.getHiveCli(), test, properties.getType() });
                    i++;
                }
            }
        }

        return testProperties;
    }
    
    private String id;
    private String file;
    private Properties args;
    private ArrayList<String[]> hiveCli;
    private TestClass test;
    private String type;
    
    public static boolean initOnly = false;
    
    public ParameterizedTests(String id, String file, Properties args, ArrayList<String[]> hiveCli, TestClass test, String type) {
        this.id = id;
        this.file = file;
        this.args = args;
        this.test = test;
        this.hiveCli = hiveCli;
        this.type = type;

    }
    
    HiveExecutor hiveExecutor = new HiveExecutor();
    
    @Before
    public void setUp() throws Exception {
        org.junit.Assume.assumeFalse("Initialization only run: " + initOnly, initOnly);
        // execute hive cli
        
        if(System.getProperty("hive.metastore.uris") == null){
            hiveExecutor.cleanup(this.id);
            hiveExecutor.setMetastoreConfig(this.id);
        }

        for(String[] hiveCliArgs:hiveCli){
            hiveExecutor.execHive(hiveCliArgs);
        }
      
        // execute script
        switch(this.type){
              case "PIG": 
                  AppExecutor exec = new PigExecutor();
                  exec.execScript(this.file, this.args);
                  break;
              case "HIVE": 
                  hiveExecutor.execScript(this.file, this.args);
                  break;
        }
        
    }

    @After
    public void cleanup() throws Exception {
        hiveExecutor.cleanup(this.id);
    }
    
    @Test
    public void test() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        org.junit.Assume.assumeFalse("Initialization only run: " + initOnly, initOnly);
        
        if(test != null){
            ResultValidator testExecutor = ResultValidatorFactory.get(test.getName());
            testExecutor.setId(this.id);
            testExecutor.setArgs(test.getArgs());
            testExecutor.validate(); 
        }
    }
}
