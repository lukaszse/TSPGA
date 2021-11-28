package pl.com.seremak.TSPGA.service;

import io.vavr.collection.Array;
import jakarta.inject.Singleton;
import lombok.Data;
import pl.com.seremak.TSPGA.model.Population;
import pl.com.seremak.TSPGA.model.Route;

import java.util.Collections;
import java.util.Random;

@Singleton
@Data
public class Mutation {

    private final Random random;
    private double mutationProbability;

    public Mutation() {
        this.random = new Random();
    }

    public Population performMutation(final Population population) {
        var mutatedRoutes = population
                .getRoutes()
                .map(this::mutateRoute)
                .collect(Array.collector());
        return Population.of(mutatedRoutes);
    }

    private Route mutateRoute(final Route route) {
        var mutatedRoute = new Route(route);
        for (int i = 0; i < route.getLocations().length(); i++) {
            mutatedRoute = swapDrawnLocation(i, mutatedRoute);
        }
        return mutatedRoute;
    }

    private Route swapDrawnLocation(final int index, final Route route) {
        return drawnToMutation() ?
                swapLocation(index, route) :
                route;
    }

    private Route swapLocation(final int index, final Route route) {
        var locations = route.getLocations().toJavaList();
        Collections.swap(locations, index, drawIndex(locations.size()));
        return Route.of(Array.ofAll(locations));
    }

    private boolean drawnToMutation() {
        return random.nextFloat() <= mutationProbability;
    }

    private int drawIndex(int locationsNumber) {
        return random.nextInt(locationsNumber);
    }
}
