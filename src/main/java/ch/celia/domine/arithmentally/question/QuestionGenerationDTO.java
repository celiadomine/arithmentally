package ch.celia.domine.arithmentally.question;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionGenerationDTO {
    @NotBlank
    private String operations;

    @Min(0)
    private int min;

    @Min(1)
    private int max;

    @Min(1)
    private int amount;
}
