package pl.com.seremak.LocationReader;

import io.micronaut.core.util.StringUtils;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import pl.com.seremak.model.Location;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public class LocationReader {

    public static List<Location> readLocation(final String stringPath) {
        var path = Paths.get(stringPath);
        var lines = FileLineReader.readFile(path);
        return lines.map(LocationReader::mapLineToLocation)
                .filter(Objects::nonNull);
    }

    public static Location mapLineToLocation(final String stringLine) {

        return Optional.of(stringLine)
                .filter(aStringLine -> stringLine.matches("\\s*\\d+\\s+\\d+\\s+\\d+"))
                .map(aStringLine -> stringLine.split("\\s+"))
                .map(Array::of)
                .map(stringArray -> stringArray.removeFirst(string -> string.equals(StringUtils.EMPTY_STRING)))
                .map(stringArray -> new Location(parseInt(stringArray.get(0)), parseInt(stringArray.get(1)), parseInt(stringArray.get(2))))
                .orElse(null);
    }

}

