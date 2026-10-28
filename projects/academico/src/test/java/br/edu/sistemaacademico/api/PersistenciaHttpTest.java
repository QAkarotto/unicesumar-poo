package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.AplicacaoAcademica;
import br.edu.sistemaacademico.BancoDeTeste;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Cenários da Atividade 03 - 2BIM: cadastro e consulta de alunos e disciplinas
 * persistidos em SQLite, inclusive após reiniciar a aplicação.
 */
class PersistenciaHttpTest {
    private static final HttpClient CLIENTE = HttpClient.newHttpClient();
    private static final ObjectMapper JSON = new ObjectMapper();

    private String urlBanco;
    private ConfigurableApplicationContext contexto;
    private String baseUrl;

    @BeforeEach
    void iniciar() throws Exception {
        urlBanco = BancoDeTeste.criar();
        iniciarAplicacao();
    }

    @AfterEach
    void encerrar() {
        encerrarAplicacao();
    }

    @AfterAll
    static void fecharCliente() {
        CLIENTE.close();
    }

    @Test
    void cadastraEConsultaAluno() throws Exception {
        var criado = post("/api/alunos", """
                {"registroAcademico":"RA2026100","nome":"Carla Mendes","email":"carla.mendes@example.com"}
                """);
        assertEquals(201, criado.statusCode());
        assertEquals("/api/alunos/RA2026100", criado.headers().firstValue("Location").orElseThrow());
        assertEquals("Carla Mendes", json(criado).get("nome").asText());

        var consultado = get("/api/alunos/RA2026100");
        assertEquals(200, consultado.statusCode());
        assertEquals(json(criado), json(consultado));
        assertEquals("carla.mendes@example.com", json(consultado).get("email").asText());
    }

    @Test
    void consultaAlunoDosDadosIniciais() throws Exception {
        var resposta = get("/api/alunos/RA2026002");
        assertEquals(200, resposta.statusCode());
        assertEquals("Aluno Exemplo Dois", json(resposta).get("nome").asText());
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
    void impedeCadastroDuplicado() throws Exception {
        var aluno = """
                {"registroAcademico":"RA2026001","nome":"Outro Nome","email":"outro@example.com"}
                """;
        var alunoDuplicado = post("/api/alunos", aluno);
        assertEquals(409, alunoDuplicado.statusCode());
        assertTrue(json(alunoDuplicado).has("mensagem"));
        assertEquals("Aluno Exemplo Um", json(get("/api/alunos/RA2026001")).get("nome").asText(),
                "O registro original não pode ser alterado.");

        var disciplina = """
                {"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":60}
                """;
        assertEquals(201, post("/api/disciplinas", disciplina).statusCode());
        var disciplinaDuplicada = post("/api/disciplinas", disciplina);
        assertEquals(409, disciplinaDuplicada.statusCode());
        assertTrue(json(disciplinaDuplicada).has("mensagem"));
    }

    @Test
    void retorna404ParaRegistrosInexistentes() throws Exception {
        for (var rota : new String[]{"/api/alunos/RA9999999", "/api/disciplinas/INEXISTENTE"}) {
            var resposta = get(rota);
            assertEquals(404, resposta.statusCode(), rota);
            assertTrue(json(resposta).has("mensagem"));
        }
    }

    @Test
    void retorna400ParaDadosInvalidos() throws Exception {
        var alunosInvalidos = new String[]{
                "{}", "", "{",
                "{\"registroAcademico\":\"RA2026200\",\"nome\":\"Sem Email\"}",
                "{\"registroAcademico\":\"RA2026200\",\"nome\":\"Email Ruim\",\"email\":\"invalido\"}",
                "{\"registroAcademico\":\" \",\"nome\":\"Sem RA\",\"email\":\"a@b.com\"}"
        };
        for (var corpo : alunosInvalidos) {
            var resposta = post("/api/alunos", corpo);
            assertEquals(400, resposta.statusCode(), corpo);
            assertTrue(json(resposta).has("mensagem"));
        }

        var disciplinasInvalidas = new String[]{
                "{}", "",
                "{\"codigo\":\"X1\",\"nome\":\"Sem Carga\"}",
                "{\"codigo\":\"X1\",\"nome\":\"Carga Zero\",\"cargaHoraria\":0}",
                "{\"codigo\":\"X1\",\"nome\":\"Carga Texto\",\"cargaHoraria\":\"oitenta\"}",
                "{\"codigo\":\"\",\"nome\":\"Sem Codigo\",\"cargaHoraria\":40}"
        };
        for (var corpo : disciplinasInvalidas) {
            var resposta = post("/api/disciplinas", corpo);
            assertEquals(400, resposta.statusCode(), corpo);
            assertTrue(json(resposta).has("mensagem"));
        }
        assertEquals(404, get("/api/alunos/RA2026200").statusCode(), "Nada inválido deve ser gravado.");
        assertEquals(404, get("/api/disciplinas/X1").statusCode(), "Nada inválido deve ser gravado.");
    }

    @Test
    void mantemDadosAposReiniciarAplicacao() throws Exception {
        assertEquals(201, post("/api/alunos", """
                {"registroAcademico":"RA2026100","nome":"Carla Mendes","email":"carla.mendes@example.com"}
                """).statusCode());
        assertEquals(201, post("/api/disciplinas", """
                {"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":60}
                """).statusCode());

        reiniciarAplicacao();

        var aluno = get("/api/alunos/RA2026100");
        assertEquals(200, aluno.statusCode());
        assertEquals("Carla Mendes", json(aluno).get("nome").asText());
        var disciplina = get("/api/disciplinas/ES");
        assertEquals(200, disciplina.statusCode());
        assertEquals(60, json(disciplina).get("cargaHoraria").asInt());
    }

    /**
     * Executa a Collection da atividade como o Postman faria: a primeira pasta,
     * reinício da aplicação, e a segunda pasta.
     */
    @Test
    void executaCollectionPostmanComReinicio() throws Exception {
        var collection = JSON.readTree(Files.readString(
                Path.of("postman/atividade-03-persistencia.postman_collection.json")));
        var pastas = collection.get("item");
        assertEquals(2, pastas.size());
        executarPasta(pastas.get(0));
        reiniciarAplicacao();
        executarPasta(pastas.get(1));
    }

    private void executarPasta(JsonNode pasta) throws Exception {
        var status = Pattern.compile("status\\((\\d+)\\)");
        var campo = Pattern.compile("json\\(\\)\\.(\\w+)\\)\\.to\\.eql\\('([^']*)'\\)");
        var propriedade = Pattern.compile("have\\.property\\('(\\w+)'\\)");

        for (var item : pasta.get("item")) {
            var nome = item.get("name").asText();
            var request = item.get("request");
            var rota = request.get("url").asText().replace("{{baseUrl}}", "");
            var resposta = request.get("method").asText().equals("POST")
                    ? post(rota, request.get("body").get("raw").asText())
                    : get(rota);

            var script = item.get("event").get(0).get("script").get("exec");
            var esperado = status.matcher(script.get(0).asText());
            assertTrue(esperado.find(), nome);
            assertEquals(Integer.parseInt(esperado.group(1)), resposta.statusCode(), nome);

            var verificacao = script.get(1).asText();
            var igual = campo.matcher(verificacao);
            var temCampo = propriedade.matcher(verificacao);
            if (igual.find()) {
                assertEquals(igual.group(2), json(resposta).get(igual.group(1)).asText(), nome);
            } else {
                assertTrue(temCampo.find(), nome);
                assertTrue(json(resposta).has(temCampo.group(1)), nome);
            }
        }
    }

    private void iniciarAplicacao() {
        contexto = SpringApplication.run(AplicacaoAcademica.class, "--server.port=0",
                "--app.database.url=" + urlBanco);
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();
    }

    private void encerrarAplicacao() {
        if (contexto != null) {
            contexto.close();
            contexto = null;
        }
    }

    private void reiniciarAplicacao() {
        encerrarAplicacao();
        iniciarAplicacao();
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
