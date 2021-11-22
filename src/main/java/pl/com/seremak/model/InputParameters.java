package pl.com.seremak.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputParameters {

    private final int runsNumber;
    private final int populationsNumber;
    private final int individualsNumber;
    private final double interbreedingProbability;
    private final double mutationProbability;
}
