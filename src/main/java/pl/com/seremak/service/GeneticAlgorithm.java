package pl.com.seremak.service;

import io.vavr.collection.Array;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.LocationReader.LocationReader;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;
import pl.com.seremak.model.Route;

import java.util.Comparator;

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
        var lastPopulation = getLastGeneration(population);
        var shortestRoute = getShortestRoute(lastPopulation);
        log.info("Shortest route has length={}", shortestRoute.getRouteLength());
        log.info("Shortest route {}", shortestRoute);
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
