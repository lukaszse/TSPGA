package pl.com.seremak.service;

import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.LocationReader.LocationReader;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;
import pl.com.seremak.model.Route;

import java.time.Instant;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
@Data
@RequiredArgsConstructor
public class GeneticAlgorithm {

    private Route locations;
    private InputParameters params;
    private final Interbreeding interbreeding;
    private final Mutation mutation;
    private final Selection selection;

    public void run(final String inputFilePath) {
        setup(params);
        locations = Route.of(LocationReader.readLocation(inputFilePath));
        var population = Population.of(locations, params.getIndividualsNumber());
//        var lastPopulation = generatePopulation(population, 300);
        var lastPopulation = getLastGeneration(population);
        var shortestRoute = getShortestRoute(lastPopulation);
        log.info("Shortest route has length={}", shortestRoute.getRouteLength());
        log.info("Shortest route {}", shortestRoute);
    }

    private Population generatePopulation(final Population population, final int seconds) {
        long startTime = Instant.now().toEpochMilli();
        long epochEndTime = Instant.now().toEpochMilli() + TimeUnit.SECONDS.toMillis(seconds);;
        Population temp = population;
        while(Instant.now().toEpochMilli() <= epochEndTime) {
            temp = createNextGeneration(temp);
            long duration = Instant.now().toEpochMilli() - startTime;
            if(duration % 5000 > 0 && duration % 5000 < 10) {
                Comparator<Route> compareByLength = Comparator.comparing(Route::getRouteLength);
                var bestRouteLength = temp.getRoutes()
                        .minBy(compareByLength)
                        .get()
                        .getRouteLength();
                var routes = temp.getRoutes()
                        .map(Route::getRouteLength)
                        .collect(List.collector());
                log.info("Duration {} seconds", duration/1000);
                log.info("Best route: {}", bestRouteLength);
                log.info("All routes: {}", routes);
            }
        }
        return temp;
    }

    private Population getLastGeneration(final Population population) {
        return Stream.rangeClosed(1, params.getPopulationsNumber())
                .map(__ -> createNextGeneration(population))
                .last();
    }

    private Population createNextGeneration(final Population population) {
        return Stream.of(population)
                .map(interbreeding::performInterbreeding)
                .map(mutation::performMutation)
                .map(selection::selectNewPopulation)
                .get();
    }

    private void setup(final InputParameters params) {
        interbreeding.setInterbreedingProbability(params.getInterbreedingProbability());
        mutation.setMutationProbability(params.getMutationProbability());
        selection.setEliteSelectionFactor(params.getEliteSelectionFactor());
    }

    private Route getShortestRoute(final Population population) {
        Comparator<Route> compareByLength = Comparator.comparing(Route::getRouteLength);
        return population.getRoutes()
                .minBy(compareByLength)
                .get();
    }

}
