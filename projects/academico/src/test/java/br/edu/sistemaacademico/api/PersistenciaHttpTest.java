package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.AplicacaoAcademica;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

// cenarios obrigatorios da atividade 03 com servidor real e um banco SQLite em pasta temporaria
class PersistenciaHttpTest {
    private static final HttpClient CLIENTE = HttpClient.newHttpClient();
    private static final ObjectMapper JSON = new ObjectMapper();

    @TempDir
    Path pasta;
    private ConfigurableApplicationContext contexto;
    private String baseUrl;

    @BeforeEach
    void iniciar() {
        subir();
    }

    @AfterEach
    void encerrar() {
        if (contexto != null) contexto.close();
    }

    @AfterAll
    static void fecharCliente() {
        CLIENTE.close();
    }

    @Test
    void cadastraEConsultaAluno() throws Exception {
        var criado = post("/api/alunos", """
                {"registroAcademico":"RA2026010","nome":"Igor Teste","email":"igor@example.com"}
                """);
        assertEquals(201, criado.statusCode());
        assertEquals("/api/alunos/RA2026010", criado.headers().firstValue("Location").orElseThrow());
        var consultado = get("/api/alunos/RA2026010");
        assertEquals(200, consultado.statusCode());
        assertEquals("Igor Teste", json(consultado).get("nome").asText());
        assertEquals("igor@example.com", json(consultado).get("email").asText());
    }

    @Test
    void cadastraEConsultaDisciplina() throws Exception {
        var criada = post("/api/disciplinas", """
                {"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":60}
                """);
        assertEquals(201, criada.statusCode());
        assertEquals("/api/disciplinas/ES", criada.headers().firstValue("Location").orElseThrow());
        var consultada = get("/api/disciplinas/ES");
        assertEquals(200, consultada.statusCode());
        assertEquals("Engenharia de Software", json(consultada).get("nome").asText());
        assertEquals(60, json(consultada).get("cargaHoraria").asInt());
    }

    @Test
    void consultaDosDadosIniciaisCarregadosNoBanco() throws Exception {
        assertEquals(200, get("/api/alunos/RA2026002").statusCode());
        assertEquals("Banco de Dados", json(get("/api/disciplinas/BD")).get("nome").asText());
    }

    @Test
    void registroInexistenteDa404() throws Exception {
        for (var rota : new String[]{"/api/alunos/RA0000000", "/api/disciplinas/XYZ"}) {
            var resposta = get(rota);
            assertEquals(404, resposta.statusCode(), rota);
            assertTrue(json(resposta).has("mensagem"));
        }
    }

    @Test
    void cadastroDuplicadoDa409ESemAlterarORegistro() throws Exception {
        var aluno = """
                {"registroAcademico":"RA2026001","nome":"Outro Nome","email":"outro@example.com"}
                """;
        var resposta = post("/api/alunos", aluno);
        assertEquals(409, resposta.statusCode());
        assertTrue(json(resposta).get("mensagem").asText().contains("RA2026001"));
        assertEquals("Aluno Exemplo Um", json(get("/api/alunos/RA2026001")).get("nome").asText());

        var disciplina = post("/api/disciplinas", """
                {"codigo":"POO","nome":"Outra","cargaHoraria":40}
                """);
        assertEquals(409, disciplina.statusCode());
        assertEquals(80, json(get("/api/disciplinas/POO")).get("cargaHoraria").asInt());
    }

    // o RA com espacos e gravado ja aparado pelo dominio, entao " RA2026001 " tambem e duplicado
    @Test
    void duplicadoComEspacosTambemDa409() throws Exception {
        var resposta = post("/api/alunos", """
                {"registroAcademico":" RA2026001 ","nome":"Aluno","email":"a@example.com"}
                """);
        assertEquals(409, resposta.statusCode());
    }

    // o e-mail nao e unico no dominio, entao dois alunos podem ter o mesmo
    @Test
    void emailRepetidoEmOutroAlunoEPermitido() throws Exception {
        var resposta = post("/api/alunos", """
                {"registroAcademico":"RA2026011","nome":"Aluno Novo","email":"aluno1@example.com"}
                """);
        assertEquals(201, resposta.statusCode());
    }

    @Test
    void dadosInvalidosDao400() throws Exception {
        String[][] casos = {
                {"/api/alunos", "{}"},
                {"/api/alunos", "{\"registroAcademico\":\"RA1\",\"nome\":\"A\",\"email\":\"sem-arroba\"}"},
                {"/api/alunos", "{\"registroAcademico\":\" \",\"nome\":\"A\",\"email\":\"a@example.com\"}"},
                {"/api/alunos", "{"},
                {"/api/disciplinas", "{\"codigo\":\"X\",\"nome\":\"X\"}"},
                {"/api/disciplinas", "{\"codigo\":\"X\",\"nome\":\"X\",\"cargaHoraria\":0}"},
                {"/api/disciplinas", "{\"codigo\":\"X\",\"nome\":\"X\",\"cargaHoraria\":\"oitenta\"}"},
                {"/api/disciplinas", ""},
        };
        for (var caso : casos) {
            var resposta = post(caso[0], caso[1]);
            assertEquals(400, resposta.statusCode(), caso[1]);
            assertTrue(json(resposta).has("mensagem"), caso[1]);
        }
        assertEquals(404, get("/api/disciplinas/X").statusCode());
    }

    @Test
    void dadosContinuamDisponiveisDepoisDeReiniciar() throws Exception {
        assertEquals(201, post("/api/alunos", """
                {"registroAcademico":"RA2026020","nome":"Persistente","email":"p@example.com"}
                """).statusCode());
        assertEquals(201, post("/api/disciplinas", """
                {"codigo":"IA","nome":"Inteligência Artificial","cargaHoraria":40}
                """).statusCode());

        contexto.close();
        subir();

        assertEquals("Persistente", json(get("/api/alunos/RA2026020")).get("nome").asText());
        assertEquals("Inteligência Artificial", json(get("/api/disciplinas/IA")).get("nome").asText());
        // reiniciar nao pode rodar o schema de novo (ele apaga as tabelas) nem duplicar os dados iniciais
        assertEquals(200, get("/api/alunos/RA2026001").statusCode());
    }

    // aluno cadastrado pela API pode ser matriculado: a matricula busca o aluno no Repository
    @Test
    void alunoCadastradoPodeSerMatriculado() throws Exception {
        post("/api/alunos", """
                {"registroAcademico":"RA2026030","nome":"Novo","email":"n@example.com"}
                """);
        var matricula = post("/api/matriculas", """
                {"registroAcademico":"RA2026030","ofertaId":"POO-2026-2"}
                """);
        assertEquals(201, matricula.statusCode());
        assertEquals("RA2026030", json(matricula).get("aluno").get("registroAcademico").asText());
    }

    private void subir() {
        contexto = SpringApplication.run(AplicacaoAcademica.class, "--server.port=0",
                "--app.database.url=jdbc:sqlite:" + pasta.resolve("persistencia.db"));
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();
    }

    private HttpResponse<String> get(String rota) throws Exception {
        return CLIENTE.send(HttpRequest.newBuilder(URI.create(baseUrl + rota)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String rota, String corpo) throws Exception {
        return CLIENTE.send(HttpRequest.newBuilder(URI.create(baseUrl + rota))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(corpo)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private static JsonNode json(HttpResponse<String> resposta) {
        return JSON.readTree(resposta.body());
    }
}
