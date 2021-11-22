package pl.com.seremak.LocationReader;

import io.vavr.collection.Array;
import pl.com.seremak.model.Location;
import pl.com.seremak.model.Route;

public class RouteLengthCalculator {

    public static int calculateRouteLength(Array<Location> locationsRoute) {
        int length = 0;
        for (int i = 0; i < locationsRoute.length()-1; i++) {
            var location = locationsRoute.get(i);
            var nextLocation = locationsRoute.get(i+1);
            length += calculateDistanceBetweenLocations(location, nextLocation);
        }
        return length;
    }

    private static int calculateDistanceBetweenLocations(final Location locationA, final Location locationB) {
        return Math.abs(locationA.getX() - locationB.getX()) + Math.abs(locationA.getY() - locationB.getY());
    }
}

