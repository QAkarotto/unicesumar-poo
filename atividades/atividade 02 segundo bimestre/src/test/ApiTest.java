package br.edu.sistemaacademico.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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
}
