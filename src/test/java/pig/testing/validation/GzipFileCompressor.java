package pig.testing.validation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;

public class GzipFileCompressor implements FileCompressor {

    @Override
    public String uncompress(Path file) throws IOException {
        String lines;
        GzipCompressorInputStream buffer = new GzipCompressorInputStream(
                new BufferedInputStream(
                        new FileInputStream(file.toString())));

        lines = IOUtils.toString(buffer, "UTF-8");

        buffer.close();
        return lines;
    }

}
