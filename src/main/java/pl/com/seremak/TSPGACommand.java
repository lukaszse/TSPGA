package pl.com.seremak;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.MissingParameterException;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.service.GeneticAlgorithm;

@Command(name = "TSPGA", description = "Traveling Salesman Problem Genetic Algorithm",
        mixinStandardHelpOptions = true)
public class TSPGACommand implements Runnable {

    @Inject
    GeneticAlgorithm geneticAlgorithm;

    @Option(names = {"-f", "--inputFile"}, description = "path of input file", required = true)
    String inputFilePath;

    @Option(names = {"-d", "--duration"}, description = "Duration in seconds", defaultValue = "10")
    int duration;

    @Option(names = {"-i", "--individuals"}, description = "Number of individuals in population (population size)", defaultValue = "10")
    int individualsNumber;

    @Option(names = {"-t", "--interbreeding"}, description = "Interbreeding probability of individuals", defaultValue = "0.8")
    double interbreedingProbability;

    @Option(names = {"-m", "--mutation"}, description = "Mutation probability of individuals", defaultValue = "0.07")
    double mutationProbability;

    @Option(names = {"-e", "--eliteSelectionFactor"}, description = "Elite selection factor", defaultValue = "0.3")
    double eliteSelectionFactor;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(TSPGACommand.class, args);
    }

    public void run() {
        geneticAlgorithm.setParams(
                InputParameters.builder()
                        .duration(duration)
                        .individualsNumber(individualsNumber)
                        .interbreedingProbability(interbreedingProbability)
                        .mutationProbability(mutationProbability)
                        .eliteSelectionFactor(eliteSelectionFactor)
                        .build());
        geneticAlgorithm.run(inputFilePath);
    }
}
