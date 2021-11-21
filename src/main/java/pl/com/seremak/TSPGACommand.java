package pl.com.seremak;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pl.com.seremak.service.GeneticAlgorithm;

@Command(name = "TSPGA", description = "...",
        mixinStandardHelpOptions = true)
public class TSPGACommand implements Runnable {

    @Inject
    GeneticAlgorithm geneticAlgorithm;

    @Option(names = {"-i", "--input"}, description = "path of input file")
    String inputFilePath;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(TSPGACommand.class, args);
    }

    public void run() {
        geneticAlgorithm.setup(inputFilePath);
    }
}
