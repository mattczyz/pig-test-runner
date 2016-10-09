package pig.testing.validation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

public class DirContentEqual implements ResultValidator {

    Properties args;

    public enum Order {
        BY_FILE_NAME, BY_FILE_SIZE 
    }
    
    public void setArgs(Properties args) {
        this.args = args;
    }

    public void validate() {
        String expectedPath = args.getProperty("expected");
        String outputPath = args.getProperty("output");

        Order order = args.getProperty("order") == null ? Order.BY_FILE_NAME : Order.valueOf(args.getProperty("order"));
        String compressor = args.getProperty("compressor") == null ? "" : args.getProperty("compressor");

        switch (order) {
        case BY_FILE_SIZE:
            byFileSize(expectedPath, outputPath, compressor);
            break;
        default:
            byFileName(expectedPath, outputPath, compressor);
            break;
        }
        
    }
    
    private void byFileName(String expectedPath, String outputPath, String compressorType){
        try {
            Iterator<Path> exepectedFiles = Files.list(
                    FileSystems.getDefault().getPath(expectedPath)).iterator();

            while (exepectedFiles.hasNext()) {
                Path exepectedFile = exepectedFiles.next();
                Path resultFile = FileSystems.getDefault().getPath(outputPath,
                        exepectedFile.toFile().getName());

                assertEquals(FileCompressorFactory.get(compressorType).uncompress(exepectedFile), 
                             FileCompressorFactory.get(compressorType).uncompress(resultFile));
            }
        } catch (IOException e) {
            throw new AssertionError("IOExeption: ", e);
        }
    }
    
    private void byFileSize(String expectedPath, String outputPath, String compressorType){
        try {

            Path[] exepected = Files.list(
                FileSystems.getDefault().getPath(expectedPath))
                .sorted((e1, e2) ->  {
                    try {
                        return Long.compare(Files.size(e1), Files.size(e2));
                    } catch (Exception e) {
                        throw new AssertionError("IOExeption: ", e);
                    }})
                    .toArray(Path[]::new);

            Path[] output = Files.list(
                    FileSystems.getDefault().getPath(outputPath))
                    .filter(x -> !x.getFileName().toString().endsWith(".crc"))
                    .sorted((e1, e2) ->  {
                        try {
                            return Long.compare(Files.size(e1), Files.size(e2));
                        } catch (Exception e) {
                            throw new AssertionError("IOExeption: ", e);
                        }})
                        .toArray(Path[]::new);
            
            assertEquals("Files number mismatch: ", exepected.length, output.length);

            for(int i=0;i<exepected.length; i++){
                assertEquals(FileCompressorFactory.get(compressorType).uncompress(exepected[i]), 
                             FileCompressorFactory.get(compressorType).uncompress(output[i]));
            }

        } catch (IOException e) {
            throw new AssertionError("IOExeption: ", e);
        }
    }
}