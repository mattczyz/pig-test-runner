package pig.testing.validation;

public class FileCompressorFactory {
    public static FileCompressor get(String compressorType){
        FileCompressor compressor;

        switch (compressorType) {
        case "GZ":
            compressor = new GzipFileCompressor();
            break;
        default:
            compressor = new DefaultFileCompressor();
            break;
        }
        
        return compressor;
    }
}
