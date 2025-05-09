package ch.celia.domine.arithmentally.game;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GameStartDTO {

    @NotBlank(message = "Please provide at least one operation (e.g. +,-,*)")
    private String operations;

    @Min(value = 0, message = "Minimum range must be 0 or greater")
    private int minRange;

    @Min(value = 1, message = "Maximum range must be 1 or greater")
    private int maxRange;

    @Min(value = 1, message = "Number of questions must be at least 1")
    private int numberOfQuestions;

    public RoundConfiguration toConfiguration() {
        RoundConfiguration config = new RoundConfiguration();
        config.setOperations(operations);
        config.setMinRange(minRange);
        config.setMaxRange(maxRange);
        config.setNumberOfQuestions(numberOfQuestions);
        return config;
    }
}
