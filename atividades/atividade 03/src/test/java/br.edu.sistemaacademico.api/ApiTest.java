package br.edu.sistemaacademico.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApiTest {

    @Autowired
    MockMvc mvc;

    // cada contexto (um por teste, por causa do @DirtiesContext) usa um banco SQLite temporário novo
    @DynamicPropertySource
    static void bancoTemporario(DynamicPropertyRegistry registry) {
        registry.add("sistemaacademico.banco.caminho", () -> {
            try {
                var arquivo = Files.createTempFile("sistema-academico-teste-", ".db");
                arquivo.toFile().deleteOnExit();
                return arquivo.toString();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Test
    void consultaMatriculaExistente() throws Exception {
        mvc.perform(get("/api/matriculas/MAT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registroAcademico").value("RA2026001"))
                .andExpect(jsonPath("$.resultado").value("REPROVADO"));
    }

    @Test
    void matriculaInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/matriculas/MAT-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void consultaAlunoExistente() throws Exception {
        mvc.perform(get("/api/alunos/RA2026001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Paola Oliveira"))
                .andExpect(jsonPath("$.matriculas.length()").value(2));
    }

    @Test
    void alunoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/alunos/RA0000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criaMatricula() throws Exception {
        mvc.perform(post("/api/matriculas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registroAcademico\":\"RA2026002\",\"turma\":\"ESOFT4S-NB\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/matriculas/MAT-004"))
                .andExpect(jsonPath("$.situacao").value("ATIVA"));
    }

    @Test
    void matriculaProibidaPeloDominioRetorna422() throws Exception {
        // Paola já foi aprovada em POO
        mvc.perform(post("/api/matriculas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registroAcademico\":\"RA2026001\",\"turma\":\"ADSIS4S\",\"disciplina\":\"POO\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.mensagem").value("O aluno já foi aprovado nesta disciplina."));
    }

    @Test
    void jsonInvalidoRetorna400() throws Exception {
        mvc.perform(post("/api/matriculas").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
    }

    // ---------- Persistência: Aluno ----------

    @Test
    void cadastraEConsultaAluno() throws Exception {
        mvc.perform(post("/api/alunos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registroAcademico\":\"RA2026010\",\"nome\":\"Carla Lima\",\"email\":\"carla@email.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/alunos/RA2026010"))
                .andExpect(jsonPath("$.nome").value("Carla Lima"));

        mvc.perform(get("/api/alunos/RA2026010"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("carla@email.com"))
                .andExpect(jsonPath("$.matriculas.length()").value(0));
    }

    @Test
    void alunoDuplicadoRetorna409() throws Exception {
        var json = "{\"registroAcademico\":\"RA2026001\",\"nome\":\"Outra Pessoa\",\"email\":\"outra@email.com\"}";
        mvc.perform(post("/api/alunos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void alunoComCamposAusentesRetorna400() throws Exception {
        mvc.perform(post("/api/alunos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registroAcademico\":\"RA1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void alunoComEmailInvalidoRetorna422() throws Exception {
        mvc.perform(post("/api/alunos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registroAcademico\":\"RA1\",\"nome\":\"X\",\"email\":\"invalido\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void matriculaDeAlunoCadastradoPelaApi() throws Exception {
        mvc.perform(post("/api/alunos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"registroAcademico\":\"RA2026011\",\"nome\":\"Diego\",\"email\":\"diego@email.com\"}"))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/matriculas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"registroAcademico\":\"RA2026011\",\"turma\":\"ESOFT4S-NB\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/alunos/RA2026011"))
                .andExpect(jsonPath("$.matriculas.length()").value(1));
    }

    // ---------- Persistência: Disciplina ----------

    @Test
    void cadastraEConsultaDisciplina() throws Exception {
        mvc.perform(post("/api/disciplinas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"ALG\",\"nome\":\"Algoritmos\",\"cargaHoraria\":60}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/disciplinas/ALG"));

        mvc.perform(get("/api/disciplinas/ALG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Algoritmos"))
                .andExpect(jsonPath("$.cargaHoraria").value(60));
    }

    @Test
    void disciplinaInicialDoBanco() throws Exception {
        mvc.perform(get("/api/disciplinas/POO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cargaHoraria").value(80));
    }

    @Test
    void disciplinaInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/disciplinas/XXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void disciplinaDuplicadaRetorna409() throws Exception {
        mvc.perform(post("/api/disciplinas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"POO\",\"nome\":\"Outra\",\"cargaHoraria\":40}"))
                .andExpect(status().isConflict());
    }

    @Test
    void disciplinaComCargaInvalidaRetorna422() throws Exception {
        mvc.perform(post("/api/disciplinas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"ZZZ\",\"nome\":\"Zero\",\"cargaHoraria\":0}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void disciplinaSemCargaHorariaRetorna400() throws Exception {
        mvc.perform(post("/api/disciplinas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"ZZZ\",\"nome\":\"Sem carga\"}"))
                .andExpect(status().isBadRequest());
    }
}
