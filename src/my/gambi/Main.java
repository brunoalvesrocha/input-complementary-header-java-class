package my.gambi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) throws IOException {

        String directory = "input-your-directory-here!!!";

        Path source = Paths.get(directory);

        try {
            Files.walkFileTree(source, new MyFileVisitor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


