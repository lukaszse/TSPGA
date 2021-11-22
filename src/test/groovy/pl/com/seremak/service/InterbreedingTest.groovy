package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.model.Route
import spock.lang.Specification

import static pl.com.seremak.TestData.*

@MicronautTest
class InterbreedingTest extends Specification {

    @Inject
    Interbreeding interbreeding;

    def 'should interbreed routes' () {

        given:
        def parentRoute1 = prepareRoute1()
        def parentRoute2 = prepareRoute2()

        when:
        def childRoute = interbreeding.interbreed(parentRoute1, parentRoute2)

        then:
        childRoute instanceof Route
    }
}
