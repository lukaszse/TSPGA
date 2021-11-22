package pl.com.seremak.service;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.model.Location;
import pl.com.seremak.model.Population;
import pl.com.seremak.model.Route;

import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@Data
public class Interbreeding {

    public static final String PARENTS_LENGTH_NOT_EQUAL = "Parents length not equal";
    private final Random random;
    private double interbreedingProbability;
    private Array<Route> parentPopulation;

    Interbreeding() {
        random = new Random();
    }

    public Population performInterbreeding(final Population population) {
        parentPopulation = Array.ofAll(population.getRoutes());
        Array<Route> childPopulation = Array.empty();
        while (parentPopulation.length() > 1) {
            childPopulation = drawAndInterbreedPairs(childPopulation);
        }
        childPopulation = childPopulation.appendAll(parentPopulation);
        return Population.of(childPopulation);
    }

    private Array<Route> drawAndInterbreedPairs(Array<Route> childPopulation) {
        var interbreedingResult =
                Stream.of(drawIndividualPair())
                        .map(this::interbreedPairOrCopyParents)
                        .flatMap(result -> result.apply(Stream::of))
                        .collect(Collectors.toList());
        return childPopulation.appendAll(interbreedingResult);
    }

    private Tuple2<Route, Route> interbreedPairOrCopyParents(final Tuple2<Route, Route> drawnPair) {
        return drawToInterbreed() ?
                interbreedPair(drawnPair) :
                drawnPair;
    }

    private Tuple2<Route, Route> drawIndividualPair() {
        return Tuple.of(getAndRemove(drawIndex()), getAndRemove(drawIndex()));
    }

    private Tuple2<Route, Route> interbreedPair(final Tuple2<Route, Route> drawnPair) {
        return Tuple.of(generateSingleChild(drawnPair._1, drawnPair._2), generateSingleChild(drawnPair._2, drawnPair._1));
    }

    private Route generateSingleChild(final Route parentRoute1, final Route parentRoute2) {
        if (parentRoute1.getRouteLength() != parentRoute1.getRouteLength()) {
            log.error(PARENTS_LENGTH_NOT_EQUAL);
        }
        var locationsNumber = parentRoute1.getLocations().length();
        var cuttingPoints = drawCuttingPoints(locationsNumber);
        var subRouteFromFirstParent = parentRoute1.getLocations().subSequence(cuttingPoints.get(0), cuttingPoints.get(1) + 1);
        var remainingLocationsFromSecondParent = getRemainingLocationsFromSecondParent(parentRoute2.getLocations(), subRouteFromFirstParent, cuttingPoints);
        return Route.of(
                combineLocationsFromFirstAndSecondParent(
                        subRouteFromFirstParent,
                        remainingLocationsFromSecondParent,
                        cuttingPoints,
                        locationsNumber));
    }

    private static Array<Integer> drawCuttingPoints(final int locationsNumber) {
        final Random random = new Random();
        return Stream.rangeClosed(1, 2)
                .map(__ -> random.nextInt(locationsNumber))
                .sorted()
                .collect(Array.collector());
    }

    private List<Location> getRemainingLocationsFromSecondParent(final Array<Location> parentRoute2, final Array<Location> locationsFromParent1, final Array<Integer> cuttingPoints) {
        List<Location> remainingLocations = List.empty();
        for (int i = cuttingPoints.get(1) + 1; i < parentRoute2.length(); i++) {
            remainingLocations = remainingLocations.append(parentRoute2.get(i));
        }
        for (int i = 0; i < cuttingPoints.get(1) + 1; i++) {
            remainingLocations = remainingLocations.append(parentRoute2.get(i));
        }
        for (Location locationFromParent1 : locationsFromParent1) {
            var indexToDelete = remainingLocations.indexWhere(remainingLocation -> remainingLocation.getLocation() == locationFromParent1.getLocation());
            remainingLocations = remainingLocations.removeAt(indexToDelete);
        }
        return remainingLocations;
    }

    private Array<Location> combineLocationsFromFirstAndSecondParent(final Array<Location> subRouteFromFirstParent, final List<Location> remainingLocationsFromSecondParent, Array<Integer> cuttingPoints, final int locationsNumber) {
        var frontSubRoute = remainingLocationsFromSecondParent.subSequence(0, cuttingPoints.get(0)).toArray();
        var tailSubRoute = remainingLocationsFromSecondParent.subSequence(cuttingPoints.get(0)).toArray();
        return frontSubRoute.appendAll(subRouteFromFirstParent).appendAll(tailSubRoute);
    }

    private Route getAndRemove(final int index) {
        var route = parentPopulation.get(index);
        parentPopulation = parentPopulation.removeAt(index);
        return route;
    }

    private boolean drawToInterbreed() {
        return random.nextFloat() <= interbreedingProbability;
    }

    private int drawCuttingPoint() {
        return random.nextInt(7) + 1;
    }

    private int drawIndex() {
        return random.nextInt(parentPopulation.length());
    }
}
