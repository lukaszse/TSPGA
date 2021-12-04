package pl.com.seremak.TSPGA.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputParameters {

    private final int duration;
    private final int individualsNumber;
    private final double interbreedingProbability;
    private final double mutationProbability;
    private final double eliteSelectionFactor;
    private final boolean testMode;
}
