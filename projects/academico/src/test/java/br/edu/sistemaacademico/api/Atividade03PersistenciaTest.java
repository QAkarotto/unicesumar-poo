package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.AplicacaoAcademica;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Atividade03PersistenciaTest {

    private static final HttpClient CLIENTE = HttpClient.newHttpClient();
    private static final ObjectMapper JSON = new ObjectMapper();

    private ConfigurableApplicationContext contexto;
    private String baseUrl;

    @BeforeEach
    void iniciar() {
        contexto = SpringApplication.run(AplicacaoAcademica.class, "--server.port=0");
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();
    }

    @AfterEach
    void encerrar() {
        if (contexto != null) {
            contexto.close();
        }
    }

    @AfterAll
    static void fecharCliente() {
        CLIENTE.close();
    }

    @Test
    void cadastraEConsultaAlunoComSucesso() throws Exception {
        String ra = "RA-TESTE-" + System.nanoTime();
        String corpo = """
                {
                    "registroAcademico": "%s",
                    "nome": "Maria Oliveira",
                    "email": "maria.oliveira@example.com"
                }
                """.formatted(ra);

        HttpResponse<String> postResposta = post("/api/alunos", corpo);
        assertEquals(201, postResposta.statusCode());
        assertTrue(postResposta.headers().firstValue("Location").orElse("").contains("/api/alunos/" + ra));

        JsonNode postJson = JSON.readTree(postResposta.body());
        assertEquals(ra, postJson.get("registroAcademico").asText());
        assertEquals("Maria Oliveira", postJson.get("nome").asText());
        assertEquals("maria.oliveira@example.com", postJson.get("email").asText());

        HttpResponse<String> getResposta = get("/api/alunos/" + ra);
        assertEquals(200, getResposta.statusCode());
        JsonNode getJson = JSON.readTree(getResposta.body());
        assertEquals(ra, getJson.get("registroAcademico").asText());
        assertEquals("Maria Oliveira", getJson.get("nome").asText());
    }

    @Test
    void cadastraEConsultaDisciplinaComSucesso() throws Exception {
        String cod = "SO-" + System.nanoTime();
        String corpo = """
                {
                    "codigo": "%s",
                    "nome": "Sistemas Operacionais",
                    "cargaHoraria": 80
                }
                """.formatted(cod);

        HttpResponse<String> postResposta = post("/api/disciplinas", corpo);
        assertEquals(201, postResposta.statusCode());
        assertTrue(postResposta.headers().firstValue("Location").orElse("").contains("/api/disciplinas/" + cod));

        JsonNode postJson = JSON.readTree(postResposta.body());
        assertEquals(cod, postJson.get("codigo").asText());
        assertEquals("Sistemas Operacionais", postJson.get("nome").asText());
        assertEquals(80, postJson.get("cargaHoraria").asInt());

        HttpResponse<String> getResposta = get("/api/disciplinas/" + cod);
        assertEquals(200, getResposta.statusCode());
        JsonNode getJson = JSON.readTree(getResposta.body());
        assertEquals(cod, getJson.get("codigo").asText());
        assertEquals("Sistemas Operacionais", getJson.get("nome").asText());
        assertEquals(80, getJson.get("cargaHoraria").asInt());
    }

    @Test
    void impedeCadastroDuplicadoDeAlunoEDisciplina() throws Exception {
        String raDup = "RA-DUP-" + System.nanoTime();
        String aluno = """
                {
                    "registroAcademico": "%s",
                    "nome": "Aluno Duplicado",
                    "email": "dup@example.com"
                }
                """.formatted(raDup);
        assertEquals(201, post("/api/alunos", aluno).statusCode());
        HttpResponse<String> dupAluno = post("/api/alunos", aluno);
        assertEquals(409, dupAluno.statusCode());
        assertTrue(JSON.readTree(dupAluno.body()).has("mensagem"));

        String codDup = "DISC-DUP-" + System.nanoTime();
        String disciplina = """
                {
                    "codigo": "%s",
                    "nome": "Disciplina Duplicada",
                    "cargaHoraria": 60
                }
                """.formatted(codDup);
        assertEquals(201, post("/api/disciplinas", disciplina).statusCode());
        HttpResponse<String> dupDisciplina = post("/api/disciplinas", disciplina);
        assertEquals(409, dupDisciplina.statusCode());
        assertTrue(JSON.readTree(dupDisciplina.body()).has("mensagem"));
    }

    @Test
    void retorna404AoConsultarRecursosInexistentes() throws Exception {
        HttpResponse<String> alunoInexistente = get("/api/alunos/RA-NAO-EXISTE-NUNCA");
        assertEquals(404, alunoInexistente.statusCode());
        assertTrue(JSON.readTree(alunoInexistente.body()).has("mensagem"));

        HttpResponse<String> disciplinaInexistente = get("/api/disciplinas/COD-NAO-EXISTE-NUNCA");
        assertEquals(404, disciplinaInexistente.statusCode());
        assertTrue(JSON.readTree(disciplinaInexistente.body()).has("mensagem"));
    }

    @Test
    void retorna400ParaDadosInvalidos() throws Exception {
        // Aluno com e-mail inválido
        String alunoInvalido = """
                {
                    "registroAcademico": "RA-INV-99",
                    "nome": "Nome Valido",
                    "email": "email_invalido"
                }
                """;
        HttpResponse<String> respAluno = post("/api/alunos", alunoInvalido);
        assertEquals(400, respAluno.statusCode());

        // Disciplina com carga horária zerada/negativa
        String discInvalida = """
                {
                    "codigo": "DISC-INV-99",
                    "nome": "Nome Valido",
                    "cargaHoraria": 0
                }
                """;
        HttpResponse<String> respDisc = post("/api/disciplinas", discInvalida);
        assertEquals(400, respDisc.statusCode());
    }

    @Test
    void persisteDadosAposReiniciarAplicacao() throws Exception {
        String raPersist = "RA-PERSIST-" + System.nanoTime();
        String corpoAluno = """
                {
                    "registroAcademico": "%s",
                    "nome": "Aluno Persistente",
                    "email": "persistente@example.com"
                }
                """.formatted(raPersist);

        String codPersist = "DISC-PERSIST-" + System.nanoTime();
        String corpoDisc = """
                {
                    "codigo": "%s",
                    "nome": "Arquitetura Persistente",
                    "cargaHoraria": 120
                }
                """.formatted(codPersist);

        // 1. Cadastra na instância em execução
        assertEquals(201, post("/api/alunos", corpoAluno).statusCode());
        assertEquals(201, post("/api/disciplinas", corpoDisc).statusCode());

        // 2. Encerra a aplicação
        contexto.close();
        contexto = null;

        // 3. Executa a aplicação novamente (reinicialização)
        contexto = SpringApplication.run(AplicacaoAcademica.class, "--server.port=0");
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();

        // 4. Consulta os registros cadastrados na sessão anterior
        HttpResponse<String> getAluno = get("/api/alunos/" + raPersist);
        assertEquals(200, getAluno.statusCode());
        assertEquals("Aluno Persistente", JSON.readTree(getAluno.body()).get("nome").asText());

        HttpResponse<String> getDisc = get("/api/disciplinas/" + codPersist);
        assertEquals(200, getDisc.statusCode());
        assertEquals("Arquitetura Persistente", JSON.readTree(getDisc.body()).get("nome").asText());
        assertEquals(120, JSON.readTree(getDisc.body()).get("cargaHoraria").asInt());
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
}
