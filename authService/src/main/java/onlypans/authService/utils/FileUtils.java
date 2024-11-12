package onlypans.authService.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtils {


    /**
     * Pass the path to the file, it reads the file and returns the content as a string, else returns an empty string.
     * @param path
     * @return string of file contents
     */
    public static String readResourceString(String path){
        try {
            return Files.readString(
                    Paths.get(
                            Objects.requireNonNull(
                                    FileUtils.class.getClassLoader().getResource(path)).toURI()
                    )
            );
        } catch (IOException | URISyntaxException e) {
            return "";
        }
    }
}
