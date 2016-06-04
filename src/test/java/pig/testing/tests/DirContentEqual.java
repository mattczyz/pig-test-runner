package pig.testing.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

public class DirContentEqual implements TestExecutor {

    Properties args;

    public void setArgs(Properties args) {
        this.args = args;
    }

    public void execute() {
        String expectedPath = args.getProperty("expected");
        String outputPath = args.getProperty("output");

        try {
            Iterator<Path> exepectedFiles = Files.list(
                    FileSystems.getDefault().getPath(expectedPath)).iterator();

            while (exepectedFiles.hasNext()) {
                Path exepectedFile = exepectedFiles.next();
                Path resultFile = FileSystems.getDefault().getPath(outputPath,
                        exepectedFile.toFile().getName());

                assertEquals(new String(Files.readAllBytes(exepectedFile)),
                        new String(Files.readAllBytes(resultFile)));
            }
        } catch (IOException e) {
            throw new AssertionError("IOExeption: ", e);
        }
    }

}
