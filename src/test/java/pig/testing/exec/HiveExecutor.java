package pig.testing.exec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
            throw new HiveException(e);
        }
    }
    
    public static String[] getMetastoreConfig() throws HiveException {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        ArrayList<String> hiveCliArgsMetastore = new ArrayList<String>();
        hiveCliArgsMetastore.add("--hiveconf");
        hiveCliArgsMetastore.add("hive.metastore.warehouse.dir=file:///"+FilenameUtils.separatorsToUnix(currentPath)+"/tmp/warehouse");
        
        hiveCliArgsMetastore.add("--hiveconf");
        hiveCliArgsMetastore.add("javax.jdo.option.ConnectionURL=jdbc:derby:;databaseName=tmp/metastore_db;create=true");
        
        hiveCliArgsMetastore.add("--hiveconf");
        hiveCliArgsMetastore.add("fs.default.name=file:///"+FilenameUtils.separatorsToUnix(currentPath)+"/tmp");
        //file://${user.dir}/
        return hiveCliArgsMetastore.toArray(new String[0]);
    }
    
    public static void cleanup() throws HiveException, IOException {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileUtils.deleteDirectory(new File(currentPath+"/tmp"));
    }
    
}
