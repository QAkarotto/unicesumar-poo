package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.AplicacaoAcademica;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.*;
import static org.junit.jupiter.api.Assertions.*;

class AcademicoHttpTest {
    private static ConfigurableApplicationContext contexto;
    private static String baseUrl;
    private static final HttpClient CLIENTE = HttpClient.newHttpClient();
    private static final ObjectMapper JSON = new ObjectMapper();

    @BeforeEach
    void iniciar() {
        contexto = SpringApplication.run(AplicacaoAcademica.class, "--server.port=0");
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();
    }

    @AfterEach
    void encerrar() {
        if (contexto != null) contexto.close();

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
        var criada = post(corpo);
        assertEquals(201, criada.statusCode());
        var location = criada.headers().firstValue("Location").orElseThrow();
        assertEquals("/api/matriculas/" + json(criada).get("codigo").asText(), location);
        assertEquals(json(criada), json(get(location)));
        assertEquals("ATIVA", json(criada).get("situacao").asText());
        assertEquals(400, post(corpo).statusCode());
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
            var resposta = post(corpo);
            assertEquals(400, resposta.statusCode(), corpo);
            assertTrue(json(resposta).has("mensagem"));
        }
        assertEquals(404, post("{\"registroAcademico\":\"ausente\",\"ofertaId\":\"POO-2026-2\"}").statusCode());
        assertEquals(404, post("{\"registroAcademico\":\"RA2026001\",\"ofertaId\":\"ausente\"}").statusCode());
        assertEquals(400, post("{\"registroAcademico\":\"RA2026001\",\"ofertaId\":\"POO-2026-2\"}").statusCode());
    }

    @Test
    void preservaRegraDeAprovacaoAnteriorPelaApi() throws Exception {
        var dados = contexto.getBean(DadosEmMemoria.class);
        var aluno = dados.buscarAluno("RA2026001");
        var turmaAnterior = new br.edu.sistemaacademico.domain.Turma("ANTERIOR",
                new br.edu.sistemaacademico.domain.PeriodoLetivo(2025, br.edu.sistemaacademico.domain.Semestre.SEGUNDO));
        turmaAnterior.ofertarDisciplina(new br.edu.sistemaacademico.domain.Disciplina("BD", "Banco de Dados", 80))
                .matricular("ANT-001", aluno).concluir(ResultadoAcademico.APROVADO);
        var resposta = post("{\"registroAcademico\":\"RA2026001\",\"ofertaId\":\"BD-2026-2\"}");
        assertEquals(409, resposta.statusCode());
        assertEquals("O aluno já foi aprovado nesta disciplina.", json(resposta).get("mensagem").asText());
        assertEquals(2, aluno.getMatriculas().size());
    }

    @AfterAll
    static void fecharCliente() {
        CLIENTE.close();
    }

    @Test
    void executaCenariosDaCollectionPostman() throws Exception {
        var collection = JSON.readTree(java.nio.file.Files.readString(
                java.nio.file.Path.of("postman/sistema-academico.postman_collection.json")));
        String codigo = "";
        for (var item : collection.get("item")) {
            // A coleção contém também os requests novos da Atividade 03; estes
            // são executados manualmente no Postman, enquanto este teste legado
            // valida os sete cenários originais de matrícula.
            if (item.get("name").asText().startsWith("Atividade 03 -")) continue;
            var request = item.get("request");
            var rota = request.get("url").asText().replace("{{baseUrl}}", "")
                    .replace("{{codigoMatricula}}", codigo);
            var resposta = request.get("method").asText().equals("POST")
                    ? post(request.get("body").get("raw").asText()) : get(rota);
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

    @Test
    void cadastraEConsultaAlunoEDisciplinaPersistidos() throws Exception {
        String sufixo = Long.toString(System.nanoTime());
        String ra = "RA-TEST-" + sufixo;
        String codigo = "DISC-TEST-" + sufixo;

        var alunoCriado = postJson("/api/alunos", """
                {"registroAcademico":"%s","nome":"Teste JDBC","email":"jdbc-%s@example.com"}
                """.formatted(ra, sufixo));
        assertEquals(201, alunoCriado.statusCode());
        assertEquals(ra, json(alunoCriado).get("registroAcademico").asText());
        assertEquals(200, get("/api/alunos/" + ra).statusCode());

        var disciplinaCriada = postJson("/api/disciplinas", """
                {"codigo":"%s","nome":"Disciplina de Teste","cargaHoraria":40}
                """.formatted(codigo));
        assertEquals(201, disciplinaCriada.statusCode());
        assertEquals(codigo, json(disciplinaCriada).get("codigo").asText());
        assertEquals(200, get("/api/disciplinas/" + codigo).statusCode());

        // Reinicia a aplicação e confirma que os registros permanecem no arquivo SQLite.
        contexto.close();
        contexto = SpringApplication.run(AplicacaoAcademica.class, "--server.port=0");
        baseUrl = "http://localhost:" + ((WebServerApplicationContext) contexto).getWebServer().getPort();

        var alunoDepoisDoReinicio = get("/api/alunos/" + ra);
        System.out.println("ALUNO APÓS REINÍCIO: "
                + alunoDepoisDoReinicio.statusCode() + " - " + alunoDepoisDoReinicio.body());
        assertEquals(200, alunoDepoisDoReinicio.statusCode());

        var disciplinaDepoisDoReinicio = get("/api/disciplinas/" + codigo);
        System.out.println("DISCIPLINA APÓS REINÍCIO: "
                + disciplinaDepoisDoReinicio.statusCode() + " - " + disciplinaDepoisDoReinicio.body());
        assertEquals(200, disciplinaDepoisDoReinicio.statusCode());
        assertEquals(409, postJson("/api/alunos", """
                {"registroAcademico":"%s","nome":"Duplicado","email":"dup@example.com"}
                """.formatted(ra)).statusCode());
        assertEquals(409, postJson("/api/disciplinas", """
                {"codigo":"%s","nome":"Duplicada","cargaHoraria":40}
                """.formatted(codigo)).statusCode());
        assertEquals(404, get("/api/alunos/RA-NAO-EXISTE-999").statusCode());
        assertEquals(404, get("/api/disciplinas/DISC-NAO-EXISTE-999").statusCode());
    }

    private static HttpResponse<String> postJson(String rota, String corpo) throws Exception {
        return CLIENTE.send(HttpRequest.newBuilder(URI.create(baseUrl + rota))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpo)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> get(String rota) throws Exception {
        return CLIENTE.send(HttpRequest.newBuilder(URI.create(baseUrl + rota)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> post(String corpo) throws Exception {
        return CLIENTE.send(HttpRequest.newBuilder(URI.create(baseUrl + "/api/matriculas"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(corpo)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private static JsonNode json(HttpResponse<String> resposta) {
        return JSON.readTree(resposta.body());
    }
}
