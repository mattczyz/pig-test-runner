package pig.testing.runner;

import org.apache.hadoop.hive.cli.CliDriver;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.MetaStoreUtils;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.thrift.HadoopThriftAuthBridge;


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
    
    // TODO: startMetastore needs completing
    public static void startMetastore() {
        HiveConf configuration = new HiveConf();
        try {
            MetaStoreUtils.startMetaStore(MetaStoreUtils.findFreePort(), new HadoopThriftAuthBridge(), 
                    configuration);

        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
