package pl.com.seremak.service;

import io.vavr.collection.Array;
import jakarta.inject.Singleton;
import lombok.Data;
import pl.com.seremak.model.Population;
import pl.com.seremak.model.Route;

import java.util.Random;
import java.util.function.Function;

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
                .map(this::mutateIfDrawn)
                .collect(Array.collector());
        return Population.of(mutatedRoutes);
    }

    private Route mutateIfDrawn(final Route route) {
        return drawnToMutation() ?
                mutateRoute(route) :
                route;
    }

    private Route mutateRoute(final Route route) {
        var locations = route.getLocations().toList();
        var drawnIndex1 = drawIndex(locations.length());
        var drawnIndex2 = drawIndex(locations.length());
        var location1 = locations.get(drawnIndex1);
        var location2 = locations.get(drawnIndex2);
        locations = locations.removeAt(drawnIndex1);
        locations = locations.insert(drawnIndex1, location2);
        locations = locations.removeAt(drawnIndex2);
        locations = locations.insert(drawnIndex2, location1);
        return Route.of(locations.toArray());
    }

    private boolean drawnToMutation() {
        return random.nextFloat() <= mutationProbability;
    }

    private int drawIndex(int locationsNumber) {
        return random.nextInt(locationsNumber);
    }
}
