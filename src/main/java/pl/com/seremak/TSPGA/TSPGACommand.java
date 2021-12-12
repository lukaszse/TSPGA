package pl.com.seremak.TSPGA;

import io.micronaut.configuration.picocli.PicocliRunner;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.com.seremak.TSPGA.model.InputParameters;
import pl.com.seremak.TSPGA.service.GeneticAlgorithm;

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

    @Option(names = {"-s", "--testMode"}, description = "Test mode", defaultValue = "false")
    boolean testMode;

    @Option(names = {"-r", "--runNumber"}, description = "Test mode", defaultValue = "10")
    int runNumber;

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
                        .testMode(testMode)
                        .runNumber(runNumber)
                        .inputFilePath(inputFilePath)
                        .build());
        geneticAlgorithm.run();
    }
}
