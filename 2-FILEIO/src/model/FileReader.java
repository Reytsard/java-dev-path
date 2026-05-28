package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    Path path = Paths.get("users.csv");
    List<String> list = Files.readAllLines(path);

    public FileReader() throws IOException {
    }
}
