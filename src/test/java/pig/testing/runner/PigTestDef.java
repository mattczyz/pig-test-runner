package pig.testing.runner;

import java.util.ArrayList;
import java.util.Properties;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PigTestDef {

	String id;
	String file;
	Properties args = new Properties();
	Properties expected = new Properties();
	@JsonProperty("hivecli")
	ArrayList<String[]> hiveCli = new ArrayList<String[]>();
	

    public ArrayList<String[]> getHiveCli() {
        return hiveCli;
    }

    public void setHiveCli(ArrayList<String[]> hiveCli) {
        this.hiveCli = hiveCli;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Properties getArgs() {
		return args;
	}

	public void setArgs(Properties args) {
		this.args = args;
	}

	public Properties getExpected() {
		return expected;
	}

	public void setExpected(Properties expected) {
		this.expected = expected;
	}
	
}