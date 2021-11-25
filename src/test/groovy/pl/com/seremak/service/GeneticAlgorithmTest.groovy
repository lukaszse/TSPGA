package pl.com.seremak.service

import groovy.util.logging.Slf4j
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.TestData
import pl.com.seremak.model.Population
import pl.com.seremak.model.Route
import spock.lang.Specification

import static pl.com.seremak.TestData.*

@Slf4j
@MicronautTest
class GeneticAlgorithmTest extends Specification {

    @Inject
    GeneticAlgorithm geneticAlgorithm;

    def 'should look for shortest route' () {

        given:
        def params = inputParams()

        and:
        geneticAlgorithm.setParams(inputParams())
        geneticAlgorithm.setup(inputParams())

        and:
        def population = Population.of(prepareRoute2(), 100)

        when:
        def children = geneticAlgorithm.getLastPopulation(population)

        then:
        children instanceof Population
        log.info("Best result = {}", children.getRoutes()
                .minBy(Comparator.comparing(Route::getRouteLength)).get().getRouteLength())
        log.info("Best result = {}", children.getRoutes()
                .minBy(Comparator.comparing(Route::getRouteLength)).get())
    }
}
