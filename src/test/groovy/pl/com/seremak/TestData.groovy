package pl.com.seremak

import io.vavr.collection.Array
import pl.com.seremak.model.InputParameters
import pl.com.seremak.model.Location
import pl.com.seremak.model.Route

import java.util.stream.Collectors
import java.util.stream.IntStream

class TestData {


    static def prepareRoute1() {
        return Route.of(Array.of(location3(), location2(), location1(), location4()))
    }

    static def prepareRoute2() {
        return Route.of(Array.of(location4(), location2(), location3(), location1()))
    }

    static def location1() {
        new Location(1, 1, 1)
    }

    static def location2() {
        new Location(2, 2, 2)
    }

    static def location3() {
        new Location(3, 3, 3)
    }

    static def location4() {
        new Location(4, 4, 4)
    }

    static def prepareRoute(Integer locationsNumber) {
        List<Location> locations = new ArrayList<>()
        for (i in 1.. locationsNumber) {
            locations.add(new Location(i, i, i))
        }
        return Route.of(Array.ofAll(locations) as Array<Location>)
    }

    static def inputParams(int duration) {
        InputParameters.builder()
                .individualsNumber(100)
                .duration(duration)
                .mutationProbability(0.02)
                .interbreedingProbability(0.3)
                .eliteSelectionFactor(0.02)
                .build()
    }
}