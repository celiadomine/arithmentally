package ch.celia.domine.arithmentally.question;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ch.celia.domine.arithmentally.security.Roles;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@Validated
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/generate")
    @RolesAllowed({Roles.Admin})
    public ResponseEntity<List<Question>> generateQuestions(@RequestBody QuestionGenerationDTO dto) {
        List<Question> questions = questionService.generateQuestions(dto.getOperations(), dto.getMin(), dto.getMax(), dto.getAmount());
        return ResponseEntity.ok(questions);
    }
}
