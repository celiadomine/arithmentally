package ch.celia.domine.arithmentally.game;

public class ScoreEngine {

  public static class ScoreState {
    private int score;
    private int streak;
    private int answered;
    private int wrong;
    private long totalDurationSeconds;

    public int getScore() { return score; }
    public int getStreak() { return streak; }
    public int getAnswered() { return answered; }
    public int getWrong() { return wrong; }
    public long getTotalDurationSeconds() { return totalDurationSeconds; }

    void addScore(int delta) { score = Math.max(0, score + delta); }
    void incAnswered() { answered++; }
    void incWrong() { wrong++; }
    void resetStreak() { streak = 0; }
    void incStreak() { streak++; }
    void addDuration(int sec) { totalDurationSeconds += Math.max(0, sec); }
  }

   /**
   * Apply one answer and update state.
   * Rules: +1 base if correct; +2 fast (<=2s), +1 medium (<=5s), +0 slow;
   * every 3rd consecutive correct (+2) streak bonus.
   * Wrong answer: −1, reset streak; score never below 0. Duration <0 → 0.
   * @param s mutable state
   * @param correct whether the answer was correct
   * @param durationSeconds response time in seconds
   * @return new total score after applying this answer
   */
  public int applyAnswer(ScoreState s, boolean correct, int durationSeconds) {
    int d = Math.max(0, durationSeconds);
    s.addDuration(d);
    s.incAnswered();

    if (!correct) { //cancels streak and penalizes score when wrong answer
      s.incWrong();
      s.resetStreak();
      s.addScore(-1);
      return s.getScore();
    }

    int points = 1; // adding points for fast answers
    if (d <= 2) points += 2;
    else if (d < 5) points += 1; // <-- should be d<= 5

    //streak bonus
    s.incStreak();
    if (s.getStreak() % 3 == 0) points += 2; 

    //add the points, lowest = zero
    s.addScore(points);
    return s.getScore();
  }

  /**
   * Finalize the round and grant end-of-round bonus.
   * Bonus: +5 if all answered and none wrong; +3 extra if average time <= 2.0s.
   * Adds the bonus to state and returns the granted amount.
   * @param s mutable state
   * @param totalQuestions planned number of questions (>0)
   * @return granted bonus (0, 5, or 8)
   */
  public int finalizeRound(ScoreState s, int totalQuestions) {
    int bonus = 0;
    boolean all = totalQuestions > 0 && s.getAnswered() == totalQuestions;
    if (all && s.getWrong() == 0) {
      bonus += 5; // bonus for perfect round
      double avg = (s.getAnswered() == 0) ? Double.POSITIVE_INFINITY
        : (double) s.getTotalDurationSeconds() / s.getAnswered();
      if (avg <= 2.0) bonus += 3;
    }
    if (bonus != 0) s.addScore(bonus);
    return bonus;
  }
}
