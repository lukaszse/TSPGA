package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.TestData
import pl.com.seremak.model.Population
import spock.lang.Specification

import static pl.com.seremak.TestData.*

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
        def population = Population.of(prepareRoute1(), 10)

        when:
        def children = geneticAlgorithm.getLastGeneration(population)

        then:
        children instanceof Population
    }
}
