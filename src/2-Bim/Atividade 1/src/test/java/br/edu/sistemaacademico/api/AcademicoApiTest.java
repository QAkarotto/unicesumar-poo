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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AcademicoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void buscaAlunoLivre() throws Exception {
        mockMvc.perform(get("/api/alunos/RA2403"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lia Ferreira"))
                .andExpect(jsonPath("$.ra").value("RA2403"));
    }

    @Test
    @Order(2)
    void registraMatricula() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2403\",\"turma\":\"TI-2026-1\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("MAT-11"))
                .andExpect(jsonPath("$.situacao").value("CURSANDO"));
    }

    @Test
    @Order(3)
    void buscaMatriculaCriada() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ra").value("RA2403"))
                .andExpect(jsonPath("$.turma").value("TI-2026-1"));
    }

    @Test
    @Order(4)
    void recusaMatriculaRepetida() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ra\":\"RA2403\",\"turma\":\"TI-2026-1\",\"disciplina\":\"BD\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(5)
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
}
