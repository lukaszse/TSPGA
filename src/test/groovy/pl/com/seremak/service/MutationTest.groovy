package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.model.Population
import spock.lang.Specification

import static pl.com.seremak.TestData.prepareRoute1

@MicronautTest
class MutationTest extends Specification {

    @Inject
    Mutation mutation

    def 'should mutate population of Routes' () {

        given:
        def population = Population.of(prepareRoute1(), 10)

        and:
        mutation.setMutationProbability(0.1)

        when:
        def children = mutation.performMutation(population)

        then:
        children instanceof Population
        children.getRoutes().length() == 10
    }
}
