package ch.celia.domine.arithmentally.game;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RoundConfiguration {

    private String operations;
    private int minRange;
    private int maxRange;
    private int numberOfQuestions;
}
