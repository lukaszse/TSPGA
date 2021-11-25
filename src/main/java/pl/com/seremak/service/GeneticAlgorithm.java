package pl.com.seremak.service;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.LocationReader.LocationReader;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Location;
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
    private Population temp;
    private int elapsedTime = 2;
    private boolean run = true;

    public void run(final String inputFilePath) {
        setup(params);
        locations = Route.of(LocationReader.readLocation(inputFilePath));
        var population = Population.of(locations, params.getIndividualsNumber());
        var lastPopulation = getLastPopulation(population, params.getDuration());
        var shortestRoute = getShortestRoute(lastPopulation);
        log.info("Shortest route has length={}", shortestRoute.getRouteLength());
        log.info("Shortest route {}", shortestRoute.getLocations().map(Location::getLocation));
    }

    private Population getLastPopulation(final Population population, final int seconds) {
        long startTime = Instant.now().toEpochMilli();
        long epochEndTime = Instant.now().toEpochMilli() + TimeUnit.SECONDS.toMillis(seconds);;
        temp = population;
        while(run) {
            temp = createNextGeneration(temp);
            long duration = Instant.now().toEpochMilli() - startTime;
        }
        return temp;
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
        runScheduler(params.getDuration());
    }

    private Route getShortestRoute(final Population population) {
        Comparator<Route> compareByLength = Comparator.comparing(Route::getRouteLength);
        return population.getRoutes()
                .minBy(compareByLength)
                .get();
    }

    private void startNotification(final InputParameters params) {
        log.info("Algorithm started with following params:");
        log.info("Duration time = {}", params.getDuration());
        log.info("Individuals number = {}", params.getIndividualsNumber());
        log.info("Interbreeding probability = {}", params.getInterbreedingProbability());
        log.info("Mutation probability = {}", params.getMutationProbability());
        log.info("Elite selection factor = {}", params.getEliteSelectionFactor());
    }

    private void runScheduler(final int durationInSeconds) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                notifyAboutResults();
            }
        }, 2000, 2000);

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                run = false;
            }
        }, TimeUnit.SECONDS.toMillis(durationInSeconds), 1000);
    }

    private void notifyAboutResults() {
        Comparator<Route> compareByLength = Comparator.comparing(Route::getRouteLength);
        var bestRouteLength = temp.getRoutes()
            .minBy(compareByLength)
            .get()
            .getRouteLength();
        var routes = temp.getRoutes()
            .map(Route::getRouteLength)
            .collect(List.collector());
        log.info("Duration: {}. Best route: {}", elapsedTime += 2, bestRouteLength);
    }
}
