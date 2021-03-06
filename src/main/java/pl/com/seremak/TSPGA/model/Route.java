package pl.com.seremak.TSPGA.model;

import io.vavr.collection.Array;
import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.seremak.TSPGA.RouteLengthCalculator.RouteLengthCalculator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route implements Comparable<Route> {

    private Array<Location> locations;
    private Integer routeLength;

    public static Array<Location> generateRouteWithNullLocation(final int size) {
        return Stream.range(0, size)
                .map(__ -> (Location) null)
                .collect(Array.collector());
    }

    public static Route of(final Array<Location> locations) {
        var length = RouteLengthCalculator.calculateRouteLength(locations);
        return new Route(locations, length);
    }

    public Route(final Route route) {
        this.locations = route.locations;
        this.routeLength = route.routeLength;
    }

    @Override
    public int compareTo(Route o) {
        return this.routeLength.compareTo(o.getRouteLength());
    }
}
