package pl.com.seremak.service;

import io.vavr.collection.List;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.LocationReader.LocationReader;
import pl.com.seremak.model.Location;

@Singleton
@Slf4j
public class GeneticAlgorithm {

    private List<Location> locations;

    public void setup(final String inputFilePath) {
        locations = LocationReader.readLocation(inputFilePath);
        System.out.println("hello");
    }

}
