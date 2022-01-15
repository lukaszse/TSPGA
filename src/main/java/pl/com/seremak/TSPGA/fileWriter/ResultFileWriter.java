package pl.com.seremak.TSPGA.fileWriter;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Slf4j
public class ResultFileWriter {
    public static final String DATE_PATTERN = "yyMMdd_HHmmss";
    private static final String FILE_NAME_PATTERN = "TSPGA_result_%s_%s.txt";
    private final String fileName;
    private final boolean testMode;

    public ResultFileWriter(final boolean testMode, final String sourceFilePath) {
        this.testMode = testMode;
        this.fileName = createFileName(sourceFilePath);
    }

    public void writeResult(final List<String> results) {
        if(!testMode) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                results.forEach(result -> writeNext(result, writer));
                writer.newLine();
            } catch (IOException e) {
                log.error("Cannot save file={}. Error={}", fileName, e.getMessage());
            }
        }
    }

    private String createFileName(final String sourceFilePath) {
        return Stream.of(Instant.now())
                .map(Date::from)
                .map(date -> new SimpleDateFormat(DATE_PATTERN).format(date))
                .map(date -> FILE_NAME_PATTERN.formatted(sourceFilePath.replace("/", "_"), date))
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
