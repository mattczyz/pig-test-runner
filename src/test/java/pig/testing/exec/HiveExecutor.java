package pig.testing.exec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.hadoop.hive.cli.CliDriver;
import org.apache.hadoop.hive.ql.metadata.HiveException;

public class HiveExecutor implements AppExecutor {

    String[] metastoreConfig;
    
    @Override
    public void execScript(String file, Properties args) throws HiveException {
        List<String> hiveCliArgs = propsToHiveConf(args);
        hiveCliArgs.add("-f");
        hiveCliArgs.add(file);
        
        String[] metastoreCliArgs = (String[]) ArrayUtils.addAll(metastoreConfig, hiveCliArgs.toArray(new String[hiveCliArgs.size()]));

        this.execHive(metastoreCliArgs);
    }
    
    public void execHive(String[] hiveCliArgs) throws HiveException  {
        
        try {
            CliDriver cd = new CliDriver();
            String[] metastoreCliArgs = (String[]) ArrayUtils.addAll(metastoreConfig, hiveCliArgs);
            int retVal = cd.run(metastoreCliArgs);
            if(retVal != 0 ){
                throw new HiveException();
            }
            
        } catch (Exception e) {
            throw new HiveException(e);
        }
        
    }

    public void setMetastoreConfig(String prefix) throws HiveException {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        ArrayList<String> hiveCliArgsMetastore = new ArrayList<String>();
        hiveCliArgsMetastore.add("--hiveconf");
        hiveCliArgsMetastore.add("hive.metastore.warehouse.dir=file:///"+FilenameUtils.separatorsToUnix(currentPath)+"/tmp/"+prefix+"/warehouse");
        
        hiveCliArgsMetastore.add("--hiveconf");
        hiveCliArgsMetastore.add("javax.jdo.option.ConnectionURL=jdbc:derby:;databaseName=tmp/"+prefix+"/metastore_db;create=true");
        
        hiveCliArgsMetastore.add("--hiveconf");
        hiveCliArgsMetastore.add("fs.default.name=file:///"+FilenameUtils.separatorsToUnix(currentPath)+"/tmp/"+prefix);
        //file://${user.dir}/
        metastoreConfig = hiveCliArgsMetastore.toArray(new String[0]);
    }
    
    public void cleanup(String prefix) throws IOException, InterruptedException {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileUtils.deleteDirectory(new File(currentPath+"/tmp/"+prefix));
        
    }

    private List<String> propsToHiveConf(Properties props) {
        List<String> keyValuePairs = new ArrayList<String>();

        for (Entry<Object, Object> entry : props.entrySet()) {
            keyValuePairs.add("--hiveconf");
            keyValuePairs.add(entry.getKey() + "=" + entry.getValue());
        }

        return keyValuePairs;
    }
}
