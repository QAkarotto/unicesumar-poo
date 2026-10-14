package br.edu.sistemaacademico.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AcademicoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void consultaMatriculaExistente() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("MAT-001"))
                .andExpect(jsonPath("$.aluno.identificadorAcademico").value("RA-1001"))
                .andExpect(jsonPath("$.aluno.nome").value("Marina Costa"))
                .andExpect(jsonPath("$.disciplina.codigo").value("POO"))
                .andExpect(jsonPath("$.disciplina.nome").value("Programação Orientada a Objetos"))
                .andExpect(jsonPath("$.codigoTurma").value("ADS-2026-1"))
                .andExpect(jsonPath("$.ano").value(2026))
                .andExpect(jsonPath("$.semestre").value("PRIMEIRO"))
                .andExpect(jsonPath("$.resultado", nullValue()));
    }

    @Test
    void consultaMatriculaInexistente() throws Exception {
        mockMvc.perform(get("/api/matriculas/MAT-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Matrícula não encontrada"));
    }

    @Test
    void consultaAlunoExistente() throws Exception {
        mockMvc.perform(get("/api/alunos/RA-1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identificadorAcademico").value("RA-1001"))
                .andExpect(jsonPath("$.nome").value("Marina Costa"))
                .andExpect(jsonPath("$.email").value("marina.costa@aluno.unicesumar.edu.br"))
                .andExpect(jsonPath("$.historico", hasSize(1)))
                .andExpect(jsonPath("$.historico[0].codigo").value("MAT-001"))
                .andExpect(jsonPath("$.historico[0].resultado", nullValue()));
    }

    @Test
    void consultaAlunoAprovado() throws Exception {
        mockMvc.perform(get("/api/alunos/RA-1002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pedro Alves"))
                .andExpect(jsonPath("$.historico[0].codigo").value("MAT-000"))
                .andExpect(jsonPath("$.historico[0].resultado").value("APROVADO"));
    }

    @Test
    void consultaAlunoInexistente() throws Exception {
        mockMvc.perform(get("/api/alunos/RA-9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Aluno não encontrado"));
    }

    @Test
    void criaMatricula() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": "MAT-010",
                                  "identificadorAluno": "RA-1003",
                                  "codigoTurma": "ADS-2026-1",
                                  "codigoDisciplina": "POO"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/matriculas/MAT-010"))
                .andExpect(jsonPath("$.codigo").value("MAT-010"))
                .andExpect(jsonPath("$.aluno.identificadorAcademico").value("RA-1003"))
                .andExpect(jsonPath("$.aluno.nome").value("Julia Nunes"))
                .andExpect(jsonPath("$.resultado", nullValue()));

        mockMvc.perform(get("/api/matriculas/MAT-010"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("MAT-010"));
    }

    @Test
    void naoMatriculaAlunoJaAprovado() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": "MAT-020",
                                  "identificadorAluno": "RA-1002",
                                  "codigoTurma": "ADS-2026-1",
                                  "codigoDisciplina": "POO"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value(
                        "Aluno já foi aprovado nesta disciplina e não pode se matricular novamente"));
    }

    @Test
    void naoMatriculaAlunoJaMatriculadoNaOferta() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": "MAT-021",
                                  "identificadorAluno": "RA-1001",
                                  "codigoTurma": "ADS-2026-1",
                                  "codigoDisciplina": "POO"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Aluno já matriculado nesta oferta de disciplina"));
    }

    @Test
    void naoCriaMatriculaComCodigoVazio() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": " ",
                                  "identificadorAluno": "RA-1003",
                                  "codigoTurma": "ADS-2026-1",
                                  "codigoDisciplina": "POO"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Código da matrícula não pode ser vazio"));
    }

    @Test
    void naoCriaMatriculaComTurmaInexistente() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": "MAT-030",
                                  "identificadorAluno": "RA-1003",
                                  "codigoTurma": "TURMA-INEXISTENTE",
                                  "codigoDisciplina": "POO"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Turma não encontrada"));
    }

    @Test
    void naoCriaMatriculaComDisciplinaNaoOfertada() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": "MAT-031",
                                  "identificadorAluno": "RA-1003",
                                  "codigoTurma": "ADS-2026-1",
                                  "codigoDisciplina": "CALC1"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Disciplina não ofertada nesta turma"));
    }

    @Test
    void naoCriaMatriculaComCodigoDuplicado() throws Exception {
        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo": "MAT-001",
                                  "identificadorAluno": "RA-1003",
                                  "codigoTurma": "ADS-2026-1",
                                  "codigoDisciplina": "POO"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Já existe uma matrícula com este código"));
    }
}
