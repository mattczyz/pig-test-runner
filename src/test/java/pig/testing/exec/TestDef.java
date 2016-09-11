package pig.testing.exec;

import java.util.ArrayList;
import java.util.Properties;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDef {

	private String id;
	private String file;
	private Properties args = new Properties();
	private ArrayList<TestClass> tests = new ArrayList<TestClass>();
	private ArrayList<String[]> hiveCli = new ArrayList<String[]>();
	private String type = "PIG";
	
	static class TestClass {
	    Properties args;
	    String name = "DirContentEqual";
	    String id;
        public Properties getArgs() {
            return args;
        }
        public void setArgs(Properties args) {
            this.args = args;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        
	}
	
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

    public ArrayList<TestClass> getTests() {
        return tests;
    }

    public void setTests(ArrayList<TestClass> tests) {
        this.tests = tests;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	
}