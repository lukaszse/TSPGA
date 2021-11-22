package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.model.Population
import pl.com.seremak.model.Route
import spock.lang.Specification

import static pl.com.seremak.TestData.*

@MicronautTest
class InterbreedingTest extends Specification {

    @Inject
    Interbreeding interbreeding;

    def 'should generate a child routes' () {

        when:
        def childRoute = interbreeding.generateSingleChild(prepareRoute1(), prepareRoute2())

        then:
        childRoute instanceof Route
        childRoute.getLocations().length() == 4;
    }

    def 'should perform interbreeding for given population' () {

        given:
        def population = Population.of(prepareRoute1(), 10);

        when:
        def children = interbreeding.performInterbreeding(population)

        then:
        children instanceof Population
    }
}
