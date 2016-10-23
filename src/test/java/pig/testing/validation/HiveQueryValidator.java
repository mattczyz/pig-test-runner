package pig.testing.validation;

import java.util.Properties;

import org.apache.hadoop.hive.ql.metadata.HiveException;

import pig.testing.exec.HiveExecutor;

public class HiveQueryValidator implements ResultValidator {

    Properties args;
    String id;
    public void setArgs(Properties args) {
        this.args = args;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void validate() {

        String expectedPath = args.getProperty("expected");
        String outputPath  = args.getProperty("output");
        String outputQuery = args.getProperty("output_query");

        String outputQueryStmt = "INSERT OVERWRITE LOCAL DIRECTORY '"+ outputPath +"' " + outputQuery;

        HiveExecutor hiveExecutor = new HiveExecutor();
        
        if(System.getProperty("hive.metastore.uris") == null){
             try {
                hiveExecutor.setMetastoreConfig(this.id);
            } catch (HiveException e) {
                throw new AssertionError("HiveExeption: ", e);
            }
        }
        
        try {
            hiveExecutor.execHive(new String[] { "-e", outputQueryStmt });
        } catch (HiveException e) {
            throw new AssertionError("HiveExeption: ", e);
        }
        
        Properties validatorProps = new Properties();
        validatorProps.setProperty("expected", expectedPath);
        validatorProps.setProperty("output", outputPath);
        
        DirContentValidator testExecutor = new DirContentValidator();
        testExecutor.setId(this.id);
        testExecutor.setArgs(validatorProps);
        testExecutor.validate(); 
    }
}