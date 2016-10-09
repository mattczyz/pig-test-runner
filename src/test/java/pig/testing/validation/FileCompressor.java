package pig.testing.validation;

import java.io.IOException;
import java.nio.file.Path;

public interface FileCompressor {
    String uncompress(Path file) throws IOException;
}
