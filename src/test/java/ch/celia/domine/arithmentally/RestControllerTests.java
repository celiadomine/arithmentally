package ch.celia.domine.arithmentally;

import ch.celia.domine.arithmentally.game.GameStartDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)


class RestControllerTests {
    @Autowired
    private MockMvc api;

    @Test
    @Order(1)
    void testStartGame() throws Exception {
        GameStartDTO game = new GameStartDTO();
        game.setOperations("+,-");
        game.setMinRange(0);
        game.setMaxRange(10);
        game.setNumberOfQuestions(5);

        String body = new ObjectMapper().writeValueAsString(game);
        String token = obtainAccessToken();

        api.perform(post("/api/games/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token)
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("score")));
    }

    @Test
    @Order(2)
    void testGetGameHistory() throws Exception {
        String token = obtainAccessToken();

        api.perform(get("/api/games/history")
                .header("Authorization", "Bearer " + token)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("configuration")));
    }

    private String obtainAccessToken() {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=arithmentally&" +
                "grant_type=password&" +
                "scope=openid profile roles offline_access&" +
                "username=admin&" +
                "password=12345";

       HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> resp = rest.postForEntity("http://localhost:8080/realms/ILV/protocol/openid-connect/token", entity, String.class);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resp.getBody()).get("access_token").toString();
    }
}
    