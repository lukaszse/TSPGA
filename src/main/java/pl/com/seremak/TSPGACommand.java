package pl.com.seremak;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.service.GeneticAlgorithm;

@Command(name = "TSPGA", description = "...",
        mixinStandardHelpOptions = true)
public class TSPGACommand implements Runnable {

    @Inject
    GeneticAlgorithm geneticAlgorithm;

    @Option(names = {"-i", "--input"}, description = "path of input file")
    String inputFilePath;

    @Option(names = {"-n", "--runsNumber"}, description = "Number of program runs", defaultValue = "40")
    int runsNumber;

    @Option(names = {"-p", "--populations"}, description = "Number of populations (generations)", defaultValue = "10")
    int populationsNumber;

    @Option(names = {"-r", "--individuals"}, description = "Number of individuals in population (population size)", defaultValue = "10")
    int individualsNumber;

    @Option(names = {"-t", "--interbreeding"}, description = "Interbreeding probability of individuals", defaultValue = "0.8")
    double interbreedingProbability;

    @Option(names = {"-m", "--mutation"}, description = "Mutation probability of individuals", defaultValue = "0.07")
    double mutationProbability;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(TSPGACommand.class, args);
    }

    public void run() {
        geneticAlgorithm.setParams(
                InputParameters.builder()
                        .runsNumber(runsNumber)
                        .populationsNumber(populationsNumber)
                        .individualsNumber(individualsNumber)
                        .interbreedingProbability(interbreedingProbability)
                        .mutationProbability(mutationProbability)
                        .build());

        geneticAlgorithm.setup(inputFilePath);
    }
}
