package pl.com.seremak.service;

import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.model.Location;
import pl.com.seremak.model.Route;

import java.util.Random;

@Slf4j
@Singleton
public class Interbreeding {

    public static final String PARENTS_LENGTH_NOT_EQUAL = "Parents length not equal";

    public Route interbreed(final Route parentRoute1, final Route parentRoute2) throws Exception {
        if (parentRoute1.getRouteLength() != parentRoute1.getRouteLength()) {
            log.error(PARENTS_LENGTH_NOT_EQUAL);
            throw new Exception(PARENTS_LENGTH_NOT_EQUAL);
        }
        var locationsNumber = parentRoute1.getLocations().length();
        var cuttingPoints = drawCuttingPoints(locationsNumber);
        var subRouteFromFirstParent = parentRoute1.getLocations().subSequence(cuttingPoints.get(0), cuttingPoints.get(1)+1);
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
        for (int i = cuttingPoints.get(1)+1; i < parentRoute2.length(); i++) {
            remainingLocations = remainingLocations.append(parentRoute2.get(i));
        }
        for (int i = 0; i < cuttingPoints.get(1)+1; i++) {
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
}
