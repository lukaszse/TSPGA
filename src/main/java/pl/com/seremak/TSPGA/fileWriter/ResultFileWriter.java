package pl.com.seremak.TSPGA.fileWriter;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Slf4j
public class ResultFileWriter {
    public static final String DATE_PATTERN = "yyMMdd_HHmmss";
    private static final String FILE_NAME_PATTERN = "GA_result_%s.txt";
    private final Path fileName;
    private final boolean testMode;

    public ResultFileWriter(final boolean testMode) {
        this.testMode = testMode;
        this.fileName = createFileName();
    }

    public void writeResult(final List<String> results) {
        if(!testMode) {
            try (BufferedWriter writer = Files.newBufferedWriter(fileName)) {
                results.forEach(result -> writeNext(result, writer));
            } catch (IOException e) {
                log.error("Cannot save file={}. Error={}", fileName, e.getMessage());
            }
        }
    }

    private Path createFileName() {
        return Stream.of(Instant.now())
                .map(Date::from)
                .map(date -> new SimpleDateFormat(DATE_PATTERN).format(date))
                .map(FILE_NAME_PATTERN::formatted)
                .map(Paths::get)
                .get();
    }

    private void writeNext(final String line, final BufferedWriter writer) {
        try {
            writer.write(line);
            writer.write(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
