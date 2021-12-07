package pl.com.seremak.TSPGA.service;

import io.vavr.collection.Array;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.TSPGA.model.Population;
import pl.com.seremak.TSPGA.model.Route;

import java.util.Random;

@Singleton
@Slf4j
@Data
public class Selection {

    private final Random random;
    private double eliteSelectionFactor;


    public Selection() {
        random = new Random();
    }

    /**
     * Elite selection
     * */
    public Population selectNewPopulation(final Population population) {
        var routes = population.getRoutes();
        var elite = getElite(routes);
        var missingIndividualsNumber = routes.length() - elite.length();
        return Population.of(populateElite(elite, missingIndividualsNumber));
    }

    private Array<Route> getElite(final Array<Route> routes) {
        int eliteSize = (int) (routes.length() * eliteSelectionFactor);
        return routes
                .sorted()
                .take(eliteSize);
    }

    private Array<Route> populateElite(final Array<Route> elite, final int missingIndividualsNumber) {
        var remainingIndividuals = Stream.rangeClosed(1, missingIndividualsNumber)
                .map(__ -> drawFromElite(elite));
        return elite.appendAll(remainingIndividuals);
    }

    private Route drawFromElite(final Array<Route> elite) {
        return elite.get(drawIndex(elite.length()));
    }

    private int drawIndex(final int listSize) {
        return random.nextInt(listSize);
    }
}
