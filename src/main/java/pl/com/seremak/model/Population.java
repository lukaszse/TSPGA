package pl.com.seremak.model;

import io.vavr.collection.Array;
import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Population {

    Array<Route> routes;

    public static Population of(final Route route, final int individualsNumber) {
        return Population.of(generatePopulation(route, individualsNumber));
    }

    public static Array<Route> generatePopulation(final Route route, final int individualsNumber) {
        return Stream.rangeClosed(1, individualsNumber)
                .map(__ -> route.getLocations())
                .map(Array::shuffle)
                .map(locations -> locations)
                .map(Route::of)
                .collect(Array.collector());
    }
}
