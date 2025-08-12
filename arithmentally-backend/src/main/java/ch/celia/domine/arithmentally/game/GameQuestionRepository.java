package ch.celia.domine.arithmentally.game;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameQuestionRepository extends JpaRepository<GameQuestion, Long> {
    List<GameQuestion> findBySessionIdOrderByIdxAsc(Long sessionId);
    GameQuestion findBySessionIdAndIdx(Long sessionId, int idx);
}