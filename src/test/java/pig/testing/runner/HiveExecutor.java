package pig.testing.runner;

import org.apache.hadoop.hive.cli.CliDriver;
import org.apache.hadoop.hive.ql.metadata.HiveException;


public class HiveExecutor {
    public static void execHive(String[] hiveCliArgs) throws HiveException  {
        
        CliDriver cd = new CliDriver();
       
        try {
            int retVal = cd.run(hiveCliArgs);
            if(retVal != 0 ){
                throw new HiveException();
            }
            
        } catch (Exception e) {
            throw new HiveException();
        }
    }
}
