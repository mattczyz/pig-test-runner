package pig.testing.runner;

import java.util.Properties;

public class PigTestDef {

	String id;
	String file;
	Properties args = new Properties();
	Properties expected = new Properties();

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