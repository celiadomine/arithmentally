package ch.celia.domine.arithmentally.controller;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.player.PlayerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerControllerTests {

    @Autowired
    private MockMvc api;

    @Test
    @Order(1)
    void testGetCurrentPlayer() throws Exception {
        String token = obtainAccessToken("admin", "12345");

        api.perform(get("/api/player/me")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("email"))); // adjust if needed
    }

    private String obtainAccessToken(String username, String password) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "client_id=arithmentally&" +
                "grant_type=password&" +
                "scope=openid profile roles offline_access&" +
                "username=" + username + "&" +
                "password=" + password;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rest.postForEntity(
                "http://localhost:8080/realms/ILV/protocol/openid-connect/token", entity, String.class
        );

        JacksonJsonParser parser = new JacksonJsonParser();
        return parser.parseMap(response.getBody()).get("access_token").toString();
    }
}
