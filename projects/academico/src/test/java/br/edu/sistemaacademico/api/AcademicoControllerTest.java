package br.edu.sistemaacademico.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AcademicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveBuscarMatriculaExistente() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("MAT-1001"))
                .andExpect(jsonPath("$.aluno.registroAcademico").value("RA2026001"));
    }

    @Test
    void deveRetornar404ParaMatriculaInexistente() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-INEXISTENTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarAlunoExistente() throws Exception {
        mockMvc.perform(get("/api/alunos/RA2026001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Paola Oliveira"));
    }

    @Test
    void deveRetornar404ParaAlunoInexistente() throws Exception {
        mockMvc.perform(get("/api/alunos/RA-INEXISTENTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCriarMatriculaComDadosValidos() throws Exception {
        String json = """
                {
                    "codigo": "MAT-TEST-2001",
                    "registroAcademico": "RA2026002",
                    "codigoTurma": "ESOFT4S-NB",
                    "codigoDisciplina": "POO"
                }
                """;

        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("MAT-TEST-2001"))
                .andExpect(jsonPath("$.aluno.registroAcademico").value("RA2026002"));
    }

    @Test
    void deveRetornar409ParaCodigoDeMatriculaDuplicado() throws Exception {
        String json = """
                {
                    "codigo": "MAT-1001",
                    "registroAcademico": "RA2026002",
                    "codigoTurma": "ESOFT4S-NB",
                    "codigoDisciplina": "BD"
                }
                """;

        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornar400QuandoCamposObrigatoriosEstaoAusentes() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "codigo": "",
                                    "registroAcademico": "RA2026002",
                                    "codigoTurma": "ESOFT4S-NB",
                                    "codigoDisciplina": "BD"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404ParaAlunoInexistenteAoCriarMatricula() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "codigo": "MAT-TEST-2002",
                                    "registroAcademico": "RA-INEXISTENTE",
                                    "codigoTurma": "ESOFT4S-NB",
                                    "codigoDisciplina": "BD"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}
