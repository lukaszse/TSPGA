package pl.com.seremak.LocationReader;

import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileLineReader {

    public static List<String> readFile(final Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            log.info("Reading file={}", filePath);
            return List.ofAll(reader.lines());
        } catch (IOException e) {
            log.error("Cannot read file={}. Error={}", filePath, e.getMessage());
            return List.empty();
        }
    }
}
