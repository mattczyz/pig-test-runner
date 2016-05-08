package pig.testing.runner;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PigScriptsTests {
    @Parameters(name = "id = {0}; path = {1}")
    public static Collection<Object[]> data() throws JsonParseException,
            JsonMappingException, IOException {
        // String pathToXml = System.getProperty("path-to-xml");
        // System.out.println(pathToXml);
        String jsonInput = new String(Files.readAllBytes(Paths
                .get("pig_test_defs.json")));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode testDefsNode = mapper.readTree(jsonInput);

        Iterator<JsonNode> testDefs = testDefsNode.iterator();
        ArrayList<Object[]> testProperties = new ArrayList<Object[]>();
        while(testDefs.hasNext()){
            PigTestDef properties = mapper.readValue(testDefs.next(), PigTestDef.class);

            testProperties.add(new Object[] { properties.getId(),
                    properties.getFile(), properties.getArgs(),
                    properties.getExpected() });

        }

        return testProperties;
    }

    private String id;
    private String file;
    private Properties args;
    private Properties expected;

    public PigScriptsTests(String id, String file, Properties args,
            Properties expected) {

        this.id = id;
        this.file = file;
        this.args = args;
        this.expected = expected;
    }

    @Test
    public void test() throws IOException, ParseException, URISyntaxException {
        execPigScript(this.id, this.file, this.args);

        for (Entry<Object, Object> entry : expected.entrySet()) {
            URI resultPath = new URI(this.args.get(entry.getKey()).toString());
            Iterator<Path> exepectedFiles = Files.list(
                    FileSystems.getDefault().getPath(
                            entry.getValue().toString())).iterator();

            while (exepectedFiles.hasNext()) {
                Path exepectedFile = exepectedFiles.next();
                Path resultFile = FileSystems.getDefault().getPath(
                        resultPath.getPath(), exepectedFile.toFile().getName());

                assertEquals(new String(Files.readAllBytes(exepectedFile)),
                        new String(Files.readAllBytes(resultFile)));
            }
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
