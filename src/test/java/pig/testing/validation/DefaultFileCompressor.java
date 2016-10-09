package pig.testing.validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultFileCompressor implements FileCompressor {

    @Override
    public String uncompress(Path file) throws IOException {
        String lines;
        lines = new String(Files.readAllBytes(file));
        return lines;
    }

}
