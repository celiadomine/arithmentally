package ch.celia.domine.arithmentally.question;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuestionService {

    public List<Question> generateQuestions(String operations, int min, int max, int amount) {
        List<Question> questions = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < amount; i++) {
            int a = random.nextInt(max - min + 1) + min;
            int b = random.nextInt(max - min + 1) + min;

            String op = pickRandomOperator(operations);
            String task = a + " " + op + " " + b;
            String answer = calculate(op, a, b);

            Question q = new Question();
            q.setTask(task);
            q.setTimeTaken(0); // until answered
            q.setCorrect(false); 
            questions.add(q);
        }

        return questions;
    }

    private String pickRandomOperator(String operations) {
        String[] ops = operations.split(",");
        return ops[new Random().nextInt(ops.length)].trim();
    }

    private String calculate(String op, int a, int b) {
        return switch (op) {
            case "+" -> String.valueOf(a + b);
            case "-" -> String.valueOf(a - b);
            case "*" -> String.valueOf(a * b);
            case "/" -> b != 0 ? String.valueOf(a / b) : "0";
            default -> "0";
        };
    }

    public boolean checkAnswer(String task, String userAnswer) {
        String[] parts = task.split(" ");
        int a = Integer.parseInt(parts[0]);
        String op = parts[1];
        int b = Integer.parseInt(parts[2]);
        String correctAnswer = calculate(op, a, b);
    
        return correctAnswer.equals(userAnswer.trim());
    }
}
