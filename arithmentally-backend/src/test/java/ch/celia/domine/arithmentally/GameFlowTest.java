package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.game.dto.AnswerRequest;
import ch.celia.domine.arithmentally.game.dto.StartGameRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameFlowTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void fullGameFlow() throws Exception {
        StartGameRequest req = new StartGameRequest();
        req.playerName = "Test"; req.numQuestions = 3; req.min = 1; req.max = 3;

        // Start
        MvcResult startRes = mvc.perform(post("/api/games/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
            .andExpect(status().isOk()).andReturn();
        Long gameId = om.readTree(startRes.getResponse().getContentAsString()).get("gameId").asLong();
        assertThat(gameId).isNotNull();

        int score = 0;
        for (int i = 0; i < 3; i++) {
            // Hole aktuelle Frage
            MvcResult qRes = mvc.perform(get("/api/games/" + gameId + "/current"))
                    .andExpect(status().isOk()).andReturn();
            var node = om.readTree(qRes.getResponse().getContentAsString());
            int a = node.get("a").asInt();
            int b = node.get("b").asInt();
            int answer = a + b; // korrekt antworten

            AnswerRequest ar = new AnswerRequest();
            ar.answer = answer;
            MvcResult ansRes = mvc.perform(post("/api/games/" + gameId + "/answer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(ar)))
                .andExpect(status().isOk()).andReturn();
            var ansNode = om.readTree(ansRes.getResponse().getContentAsString());
            score = ansNode.get("score").asInt();
        }

        // Score am Ende
        MvcResult scoreRes = mvc.perform(get("/api/games/" + gameId + "/history"))
                .andExpect(status().isOk()).andReturn();
        var hist = om.readTree(scoreRes.getResponse().getContentAsString());
        assertThat(hist.get("score").asInt()).isEqualTo(3);
        assertThat(hist.get("questions").size()).isEqualTo(3);
    }
}