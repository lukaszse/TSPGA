package pl.com.seremak.TSPGA.service;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.TSPGA.fileWriter.ResultFileWriter;
import pl.com.seremak.TSPGA.locationReader.LocationReader;
import pl.com.seremak.TSPGA.model.InputParameters;
import pl.com.seremak.TSPGA.model.Location;
import pl.com.seremak.TSPGA.model.Population;
import pl.com.seremak.TSPGA.model.Route;

import java.util.Timer;
import java.util.TimerTask;
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
    ResultFileWriter writer;
    private boolean run = true;

    public void run(final String inputFilePath) {
        setup(params);
        initialInformation();
        locations = Route.of(LocationReader.readLocation(inputFilePath));
        var population = Population.of(locations, params.getIndividualsNumber());
        var lastPopulation = getLastPopulation(population);
        var shortestRoute = getShortestRoute(lastPopulation);
        notifyAboutResults();
        writer.writeResult(prepareResultString(shortestRoute));
    }

    private Population getLastPopulation(final Population population) {
        temp = population;
        while (run) {
            temp = createNextGeneration(temp);
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
        writer = new ResultFileWriter(params.isTestMode());
        runScheduler(params.getDuration());
    }

    private Route getShortestRoute(final Population population) {
        return population.getRoutes()
                .min()
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
                t.cancel();
            }
        }, TimeUnit.SECONDS.toMillis(durationInSeconds));
    }

    private List<String> prepareResultString(final Route route) {
        return route.getLocations()
                .map(Location::getLocation)
                .map(Object::toString)
                .collect(List.collector())
                .prepend(route.getRouteLength().toString());
    }

    private void notifyAboutResults() {
        var bestRouteLength = temp.getRoutes()
                .min()
                .get()
                .getRouteLength();
        log.info("Duration: {}. Best route: {}", elapsedTime += 2, bestRouteLength);
    }

    private void notifyAboutFinalResult(final Route shortestRoute) {
        log.info("Shortest route has length={}", shortestRoute.getRouteLength());
        log.info("Shortest route {}", shortestRoute.getLocations().map(Location::getLocation));
    }

    private void initialInformation() {
        log.info("Algorithm started with parameters:");
        log.info("Duration time = {}", params.getDuration());
        log.info("Interbreeding probability = {}", params.getInterbreedingProbability());
        log.info("Mutation probability = {}", params.getMutationProbability());
        log.info("Elite selection factor = {}", params.getEliteSelectionFactor());
        log.info("Numbers of individuals in population = {}", params.getIndividualsNumber());
        if (getParams().isTestMode()) {
            log.warn("WARNING! This is test mode. File will not be saved");
        }
    }
}
