package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.AplicacaoAcademica;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.persistencia.BancoDeTeste;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AcademicoHttpTest {

    private static ConfigurableApplicationContext contexto;
    private static String baseUrl;
    private static final HttpClient CLIENTE = HttpClient.newHttpClient();
    private static final ObjectMapper JSON = new ObjectMapper();

    @TempDir
    Path pasta;
    private String urlBanco;

    @BeforeEach
    void iniciar() throws Exception {
        urlBanco = BancoDeTeste.criar(pasta);
        subir();
    }

    private void subir() {
        contexto = SpringApplication.run(
                AplicacaoAcademica.class,
                "--server.port=0",
                "--app.database.url=" + urlBanco
        );
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();
    }

    @AfterEach
    void encerrar() {
        if (contexto != null) {
            contexto.close();
        }
    }

    @Test
    void consultaAlunoEMatriculaSemCiclosNoJson() throws Exception {
        var aluno = get("/api/alunos/RA2026001");
        assertEquals(200, aluno.statusCode());
        assertEquals("RA2026001", json(aluno).get("registroAcademico").asText());

        var matricula = get("/api/matriculas/MAT-001");
        assertEquals(200, matricula.statusCode());
        assertTrue(matricula.headers().firstValue("Content-Type").orElseThrow().contains("application/json"));
        assertEquals("POO", json(matricula).get("codigoDisciplina").asText());
        assertEquals("RA2026001", json(matricula).get("aluno").get("registroAcademico").asText());
    }

    @Test
    void criaConsultaEImpedeDuplicacaoSemAlterarHistorico() throws Exception {
        var corpo = """
                {"registroAcademico":"RA2026002","ofertaId":"BD-2026-2"}
                """;
        var criada = post("/api/matriculas", corpo);
        assertEquals(201, criada.statusCode());
        var location = criada.headers().firstValue("Location").orElseThrow();
        assertEquals("/api/matriculas/" + json(criada).get("codigo").asText(), location);
        assertEquals(json(criada), json(get(location)));
        assertEquals("ATIVA", json(criada).get("situacao").asText());
        assertEquals(400, post("/api/matriculas", corpo).statusCode());
        assertEquals(1, contexto.getBean(DadosEmMemoria.class).buscarAluno("RA2026002").getMatriculas().size());
    }

    @Test
    void retornaErrosJsonParaDadosInvalidosERecursosAusentes() throws Exception {
        for (var rota : new String[]{"/api/alunos/ausente", "/api/matriculas/ausente"}) {
            var resposta = get(rota);
            assertEquals(404, resposta.statusCode());
            assertTrue(json(resposta).has("mensagem"));
        }
        for (var corpo : new String[]{"{}", "null", "{", "", "{\"registroAcademico\":\" \"}"}) {
            var resposta = post("/api/matriculas", corpo);
            assertEquals(400, resposta.statusCode(), corpo);
            assertTrue(json(resposta).has("mensagem"));
        }
        assertEquals(404, post("/api/matriculas", "{\"registroAcademico\":\"ausente\",\"ofertaId\":\"POO-2026-2\"}").statusCode());
        assertEquals(404, post("/api/matriculas", "{\"registroAcademico\":\"RA2026001\",\"ofertaId\":\"ausente\"}").statusCode());
        assertEquals(400, post("/api/matriculas", "{\"registroAcademico\":\"RA2026001\",\"ofertaId\":\"POO-2026-2\"}").statusCode());
    }

    @Test
    void preservaRegraDeAprovacaoAnteriorPelaApi() throws Exception {
        var dados = contexto.getBean(DadosEmMemoria.class);
        var aluno = dados.buscarAluno("RA2026001");
        var turmaAnterior = new br.edu.sistemaacademico.domain.Turma("ANTERIOR",
                new br.edu.sistemaacademico.domain.PeriodoLetivo(2025, br.edu.sistemaacademico.domain.Semestre.SEGUNDO));
        turmaAnterior.ofertarDisciplina(new br.edu.sistemaacademico.domain.Disciplina("BD", "Banco de Dados", 80))
                .matricular("ANT-001", aluno).concluir(ResultadoAcademico.APROVADO);
        var resposta = post("/api/matriculas", "{\"registroAcademico\":\"RA2026001\",\"ofertaId\":\"BD-2026-2\"}");
        assertEquals(409, resposta.statusCode());
        assertEquals("O aluno já foi aprovado nesta disciplina.", json(resposta).get("mensagem").asText());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @Test
    void cadastroDeAlunoEDisciplinaContinuaDepoisDeReiniciarAApi() throws Exception {
        var aluno = post("/api/alunos", """
                {"registroAcademico":"RA2026010","nome":"Carla Mendes","email":"carla.mendes@example.com"}
                """);
        assertEquals(201, aluno.statusCode());
        assertEquals("/api/alunos/RA2026010", aluno.headers().firstValue("Location").orElseThrow());

        var disciplina = post("/api/disciplinas", """
                {"codigo":"ES","nome":"Engenharia de Software","cargaHoraria":60}
                """);
        assertEquals(201, disciplina.statusCode());
        assertEquals("/api/disciplinas/ES", disciplina.headers().firstValue("Location").orElseThrow());

        contexto.close();
        subir();

        var consultado = get("/api/alunos/RA2026010");
        assertEquals(200, consultado.statusCode());
        assertEquals(json(aluno), json(consultado));

        var consultada = get("/api/disciplinas/ES");
        assertEquals(200, consultada.statusCode());
        assertEquals(json(disciplina), json(consultada));
    }

    @Test
    void consultaDadosIniciaisDoBancoERegistroInexistente() throws Exception {
        var poo = get("/api/disciplinas/POO");
        assertEquals(200, poo.statusCode());
        assertEquals("Programação Orientada a Objetos", json(poo).get("nome").asText());
        assertEquals(80, json(poo).get("cargaHoraria").asInt());

        for (var rota : new String[]{"/api/alunos/RA0000000", "/api/disciplinas/ausente"}) {
            var resposta = get(rota);
            assertEquals(404, resposta.statusCode());
            assertTrue(json(resposta).has("mensagem"));
        }
    }

    @Test
    void cadastroDuplicadoDaConflitoSemAlterarOOriginal() throws Exception {
        var alunoDuplicado = post("/api/alunos",
                "{\"registroAcademico\":\"RA2026001\",\"nome\":\"Outro\",\"email\":\"outro@example.com\"}");
        assertEquals(409, alunoDuplicado.statusCode());
        assertEquals("Já existe aluno com este registro acadêmico.", json(alunoDuplicado).get("mensagem").asText());

        var disciplinaDuplicada = post("/api/disciplinas",
                "{\"codigo\":\"POO\",\"nome\":\"Outra\",\"cargaHoraria\":40}");
        assertEquals(409, disciplinaDuplicada.statusCode());
        assertEquals("Já existe disciplina com este código.", json(disciplinaDuplicada).get("mensagem").asText());

        assertEquals("Aluno Exemplo Um", json(get("/api/alunos/RA2026001")).get("nome").asText());
        assertEquals(80, json(get("/api/disciplinas/POO")).get("cargaHoraria").asInt());
    }

    @Test
    void recusaCadastroComDadosInvalidos() throws Exception {
        var invalidos = new String[][]{
                {"/api/alunos", "{\"registroAcademico\":\"RA2026009\",\"nome\":\"Sem Email\",\"email\":\"invalido\"}"},
                {"/api/alunos", "{\"registroAcademico\":\" \",\"nome\":\"Aluno\",\"email\":\"a@example.com\"}"},
                {"/api/alunos", "{}"},
                {"/api/disciplinas", "{\"codigo\":\"ES\",\"nome\":\"Engenharia de Software\",\"cargaHoraria\":0}"},
                {"/api/disciplinas", "{"}
        };

        for (var caso : invalidos) {
            var resposta = post(caso[0], caso[1]);
            assertEquals(400, resposta.statusCode(), caso[1]);
            assertTrue(json(resposta).has("mensagem"));
        }

        assertEquals(404, get("/api/alunos/RA2026009").statusCode());
        assertEquals(404, get("/api/disciplinas/ES").statusCode());
    }

    @Test
    void executaCenariosDaCollectionPostman() throws Exception {
        Path caminhoCollection = Path.of("postman/sistema-academico.postman_collection.json");
        if (!Files.exists(caminhoCollection)) {
            caminhoCollection = Path.of("projects/academico/postman/sistema-academico.postman_collection.json");
        }
        var collection = JSON.readTree(Files.readString(caminhoCollection));

        String codigo = "";
        for (var item : collection.get("item")) {
            var request = item.get("request");
            var rota = request.get("url").asText()
                    .replace("{{baseUrl}}", "")
                    .replace("{{codigoMatricula}}", codigo);
            var resposta = request.get("method").asText().equals("POST")
                    ? post(rota, request.get("body").get("raw").asText())
                    : get(rota);
            var script = item.get("event").get(0).get("script").get("exec");
            var status = java.util.regex.Pattern.compile("status\\((\\d+)\\)")
                    .matcher(script.get(0).asText());
            assertTrue(status.find());
            assertEquals(Integer.parseInt(status.group(1)), resposta.statusCode(), item.get("name").asText());
            var campo = java.util.regex.Pattern.compile("json\\(\\)\\.(\\w+)\\)\\.to\\.eql\\('([^']*)'\\)")
                    .matcher(script.get(1).asText());
            assertTrue(campo.find());
            assertEquals(campo.group(2), json(resposta).get(campo.group(1)).asText());
            if (resposta.statusCode() == 201) {
                codigo = json(resposta).get("codigo").asText();
                assertEquals("/api/matriculas/" + codigo, resposta.headers().firstValue("Location").orElseThrow());
            }
        }
    }

    @AfterAll
    static void fecharCliente() {
        CLIENTE.close();
    }

    private static HttpResponse<String> get(String rota) throws Exception {
        return CLIENTE.send(
                HttpRequest.newBuilder(URI.create(baseUrl + rota)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }

    private static HttpResponse<String> post(String rota, String corpo) throws Exception {
        return CLIENTE.send(
                HttpRequest.newBuilder(URI.create(baseUrl + rota))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(corpo))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }

    private static JsonNode json(HttpResponse<String> resposta) {
        return JSON.readTree(resposta.body());
    }
}
