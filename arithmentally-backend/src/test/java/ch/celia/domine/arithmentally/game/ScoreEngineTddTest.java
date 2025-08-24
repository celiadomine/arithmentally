package ch.celia.domine.arithmentally.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ScoreEngine – rules summary (human-readable)
 *
 * Per answer (applyAnswer):
 *  - Base points for a correct answer: +1
 *  - Speed bonus by duration d (seconds, negatives treated as 0):
 *      d <= 2  -> +2   (fast)
 *      d <= 5  -> +1   (medium)
 *      d > 5   -> +0   (slow)
 *  - Streak bonus: on every 3rd consecutive correct answer (3, 6, 9, …) -> +2
 *  - Wrong answer: -1 point, streak resets to 0, total score never below 0
 *
 * End of round (finalizeRound):
 *  - Perfect bonus: if answered == totalQuestions AND wrong == 0 -> +5
 *  - Fast-average bonus: if the above is true AND (totalDurationSeconds / answered) <= 2.0 -> +3
 *
 * Edge cases:
 *  - Duration < 0 -> treated as 0
 *  - Score is clamped at >= 0 (never goes negative)
 *  - finalizeRound only considers perfect/average bonuses when totalQuestions > 0
 */

class ScoreEngineTddTest {

  @Test
  void three_fast_correct_answers_gain_streak_bonus() {
    var s = new ScoreEngine.ScoreState();
    var e = new ScoreEngine();

    //3 fast (1s) answers --> 1 (basis) + 2 (fast) = 3
    e.applyAnswer(s, true, 1); 
    e.applyAnswer(s, true, 1); 
    e.applyAnswer(s, true, 1); //+ 2 (streak)

    assertEquals(11, s.getScore()); // 3 + 3 + 5
    assertEquals(3, s.getStreak());
    assertEquals(3, s.getAnswered());
    assertEquals(0, s.getWrong());
  }

  @Test
  void boundary_speeds_fast_medium_slow() {
    var s = new ScoreEngine.ScoreState();
    var e = new ScoreEngine();

    // test boundary values
    e.applyAnswer(s, true, 2); // fast -> +2
    e.applyAnswer(s, true, 5); // medium -> + 1
    e.applyAnswer(s, true, 6); // slow

    assertEquals(8, s.getScore()); // expectation: 3 + 2 + 1 + 2 (streak) = 8
    assertEquals(3, s.getStreak());
  }

  @Test
  void wrong_answer_penalizes_resets_streak_and_never_below_zero() {
    var s = new ScoreEngine.ScoreState();
    var e = new ScoreEngine();

    e.applyAnswer(s, false, 3); //score must never be below zero
    assertEquals(0, s.getScore());
    assertEquals(0, s.getStreak());

    e.applyAnswer(s, false, 3);
    assertEquals(0, s.getScore());
    assertEquals(0, s.getStreak());
  }

  @Test
  void negative_duration_treated_as_zero_counts_as_fast() {
    var s = new ScoreEngine.ScoreState();
    var e = new ScoreEngine();

    e.applyAnswer(s, true, -4); // -> fast
    assertEquals(3, s.getScore());
  }

  @Test
  void finalize_perfect_round_with_fast_average_adds_8() {
    var s = new ScoreEngine.ScoreState();
    var e = new ScoreEngine();

    // Ø 1.5s
    e.applyAnswer(s, true, 1);
    e.applyAnswer(s, true, 2);
    e.applyAnswer(s, true, 2);
    e.applyAnswer(s, true, 1);

    int before = s.getScore();
    int bonus = e.finalizeRound(s, 4);
    assertEquals(8, bonus);           // +5 perfect +3 fast-average
    assertEquals(before + 8, s.getScore());
  }

  @Test
  void finalize_no_bonus_if_not_all_answered_or_any_wrong() {
    var s = new ScoreEngine.ScoreState();
    var e = new ScoreEngine();

    e.applyAnswer(s, true, 1);
    e.applyAnswer(s, false, 1);
    int bonus = e.finalizeRound(s, 2);
    assertEquals(0, bonus);
  }
}
