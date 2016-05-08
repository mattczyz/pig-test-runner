package pig.testing.runner;

import java.io.IOException;

import org.apache.pig.pigunit.PigTest;

import org.apache.pig.tools.parameters.ParseException;

import org.junit.Test;

public class AppTest {	
	@Test
	public void testTop2Queries() throws IOException, ParseException {
		String[] args = { "n=2", };

		PigTest test = new PigTest("top_queries_dist.pig", args);   
		test.unoverride("STORE");
		test.unoverride("DUMP");
		test.runScript();
		
		String[] input = { "yahoo", "yahoo", "yahoo", "twitter", "facebook",
				"facebook", "linkedin", };

		String[] output = { "(yahoo,3)", "(facebook,2)", };

		test.assertOutput("data", input, "queries_limit", output);
	}

}
