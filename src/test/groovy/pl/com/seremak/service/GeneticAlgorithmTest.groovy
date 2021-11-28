package pl.com.seremak.service

import groovy.util.logging.Slf4j
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.model.Population
import pl.com.seremak.model.Route
import spock.lang.Specification

import static pl.com.seremak.TestData.*

@Slf4j
@MicronautTest
class GeneticAlgorithmTest extends Specification {

    @Inject
    GeneticAlgorithm geneticAlgorithm

    def 'should look for shortest route'() {

        given:
        def params = inputParams(5)

        and:
        geneticAlgorithm.setParams(params)
        geneticAlgorithm.setup(params)

        and:
        def population = Population.of(prepareRoute(locationsNumber), params.individualsNumber)

        when:
        def children = geneticAlgorithm.getLastPopulation(population)

        then:
        children instanceof Population
        def shortestRouteLength = children.getRoutes().minBy(Comparator.comparing(Route::getRouteLength)).get().getRouteLength()
        shortestRouteLength == expectedRouteLength
        log.info("Best result length= {}", shortestRouteLength)
        log.info("Best result = {}", children.getRoutes()
                .minBy(Comparator.comparing(Route::getRouteLength)).get())

        where:
        locationsNumber | expectedRouteLength
//        2               | 2
//        4               | 6
//        5               | 8
        15              | 28
//        12              | 20
//        50              | 98

    }
}
