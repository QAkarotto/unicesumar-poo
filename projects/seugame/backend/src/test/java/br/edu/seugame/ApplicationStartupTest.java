package br.edu.seugame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import tools.jackson.databind.json.JsonMapper;

class ApplicationStartupTest {
    @Test
    void deveIniciarServidorRealNoPerfilInfraSemAcessarBanco() throws Exception {
        try (var context = new SpringApplicationBuilder(SeuGameApplication.class)
                .profiles("infra")
                .run("--server.port=0", "--app.time-zone=America/Sao_Paulo")) {
            assertFalse(context.getBeansOfType(DataSource.class).size() > 0);
            assertEquals(ZoneId.of("America/Sao_Paulo"), context.getBean(Clock.class).getZone());
            String port = context.getEnvironment().getRequiredProperty("local.server.port");
            try (HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build()) {
                HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/status"))
                        .timeout(Duration.ofSeconds(5)).GET().build();
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response.statusCode());
                assertEquals("UP", new JsonMapper().readTree(response.body()).get("status").asString());

                var missing = client.send(HttpRequest.newBuilder(
                        URI.create("http://localhost:" + port + "/api/torneios/0")).GET().build(),
                        HttpResponse.BodyHandlers.ofString());
                assertEquals(404, missing.statusCode());
                assertEquals(404, new JsonMapper().readTree(missing.body()).get("status").asInt());
            }
        }
    }
}
