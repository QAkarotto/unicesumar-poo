package br.edu.sistemaacademico.api;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AcademicoApiTest {

    static {
        try {
            Files.deleteIfExists(Path.of("database/academico-teste.db"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void buscaMatriculaExistente() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("MAT-001"))
                .andExpect(jsonPath("$.ra").value("RA2401"));
    }

    @Test
    @Order(2)
    void buscaAlunoExistente() throws Exception {
        mockMvc.perform(get("/api/alunos/RA2401"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Helena Prado"));
    }

    @Test
    @Order(3)
    void registraMatricula() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2403\",\"turma\":\"TI-2026-1\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("MAT-11"))
                .andExpect(jsonPath("$.situacao").value("CURSANDO"));
    }

    @Test
    @Order(4)
    void buscaMatriculaCriada() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ra").value("RA2403"))
                .andExpect(jsonPath("$.turma").value("TI-2026-1"));
    }

    @Test
    @Order(5)
    void recusaMatriculaRepetida() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2403\",\"turma\":\"TI-2026-1\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(6)
    void recusaPedidoIncompleto() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2403\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void alunoInexistenteNaMatricula() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA9999\",\"turma\":\"TI-2026-1\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void consultaInexistente() throws Exception {
        mockMvc.perform(get("/api/alunos/RA9999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/matriculas/MAT-999")).andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    void cadastraAluno() throws Exception {
        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2501\",\"nome\":\"Ana Souza\",\"email\":\"ana@exemplo.edu\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ra").value("RA2501"))
                .andExpect(jsonPath("$.nome").value("Ana Souza"));
    }

    @Test
    @Order(10)
    void consultaAlunoCadastrado() throws Exception {
        mockMvc.perform(get("/api/alunos/RA2501"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@exemplo.edu"));
    }

    @Test
    @Order(11)
    void recusaAlunoDuplicado() throws Exception {
        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2501\",\"nome\":\"Ana Souza\",\"email\":\"ana@exemplo.edu\"}"))
                .andExpect(status().isConflict());
    }
}
