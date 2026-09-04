package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaDisciplinaTest {

    @Test
    @DisplayName("Deve registrar a mesma matrícula no aluno e na oferta")
    void deveRegistrarMatriculaNoAlunoENaOferta() {
        // Arrange
        var aluno = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var oferta = novaOferta("T1", 2026, Semestre.SEGUNDO,
                new Disciplina("POO", "Programação Orientada a Objetos", 80));

        // Act
        var matricula = oferta.matricular("MAT-001", aluno);

        // Assert
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
        assertSame(matricula, oferta.getMatriculas().get(0));
        assertSame(matricula, aluno.getMatriculas().get(0));
        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
        assertEquals(1, oferta.getTotalMatriculados());
    }

    @Test
    @DisplayName("Deve impedir matrícula duplicada do mesmo aluno na mesma oferta")
    void deveImpedirMatriculaDuplicada() {
        // Arrange
        var aluno = new Aluno("RA002", "Bruno Lima", "bruno.lima@email.com");
        var oferta = novaOferta("T2", 2026, Semestre.SEGUNDO,
                new Disciplina("POO", "Programação Orientada a Objetos", 80));
        oferta.matricular("MAT-002", aluno);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular("MAT-003", aluno)
        );
        assertEquals(1, oferta.getMatriculas().size());
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve impedir reutilização do mesmo código de matrícula")
    void deveImpedirCodigoDeMatriculaDuplicado() {
        // Arrange
        var aluno1 = new Aluno("RA003", "Ana", "ana@email.com");
        var aluno2 = new Aluno("RA004", "Bruno", "bruno@email.com");
        var oferta = novaOferta("T3", 2026, Semestre.SEGUNDO,
                new Disciplina("BD", "Banco de Dados", 80));
        oferta.matricular("MAT-004", aluno1);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular(" mat-004 ", aluno2));
        assertEquals(1, oferta.getTotalMatriculados());
    }

    @Test
    @DisplayName("Deve permitir nova matrícula após reprovação")
    void devePermitirNovaMatriculaAposReprovacao() {
        // Arrange
        var aluno = new Aluno("RA005", "Carla", "carla@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var oferta2025 = novaOferta("T4", 2025, Semestre.SEGUNDO, disciplina);
        var oferta2026 = novaOferta("T5", 2026, Semestre.PRIMEIRO, disciplina);

        var primeira = oferta2025.matricular("MAT-005", aluno);
        primeira.concluir(ResultadoAcademico.REPROVADO);

        // Act
        var segunda = oferta2026.matricular("MAT-006", aluno);

        // Assert
        assertEquals(SituacaoMatricula.ATIVA, segunda.getSituacao());
        assertEquals(2, aluno.getMatriculas().size());
        assertFalse(aluno.foiAprovadoEm(disciplina));
    }

    @Test
    @DisplayName("Deve bloquear nova matrícula após aprovação na disciplina")
    void deveBloquearNovaMatriculaAposAprovacao() {
        // Arrange
        var aluno = new Aluno("RA006", "Daniel", "daniel@email.com");
        var disciplina = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var oferta1 = novaOferta("T6", 2025, Semestre.SEGUNDO, disciplina);
        var oferta2 = novaOferta("T7", 2026, Semestre.PRIMEIRO, disciplina);

        var matricula = oferta1.matricular("MAT-007", aluno);
        matricula.concluir(ResultadoAcademico.APROVADO);

        // Act + Assert
        assertTrue(aluno.foiAprovadoEm(disciplina));
        assertThrows(IllegalStateException.class,
                () -> oferta2.matricular("MAT-008", aluno));
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Deve localizar matrícula do aluno e informar quando não existir")
    void deveBuscarMatriculaDoAluno() {
        // Arrange
        var aluno = new Aluno("RA007", "Eduarda", "eduarda@email.com");
        var outroAluno = new Aluno("RA008", "Felipe", "felipe@email.com");
        var oferta = novaOferta("T8", 2026, Semestre.SEGUNDO,
                new Disciplina("ENG", "Engenharia de Software", 80));
        var matricula = oferta.matricular("MAT-009", aluno);

        // Act + Assert
        assertTrue(oferta.possuiMatriculaDe(aluno));
        assertTrue(oferta.possuiMatriculaComCodigo("mat-009"));
        assertSame(matricula, oferta.buscarMatricula(aluno));
        assertFalse(oferta.possuiMatriculaDe(outroAluno));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.buscarMatricula(outroAluno));
    }

    @Test
    @DisplayName("Deve validar dados obrigatórios nas operações de matrícula")
    void deveValidarDadosObrigatoriosDaMatricula() {
        var aluno = new Aluno("RA009", "Gabriel", "gabriel@email.com");
        var oferta = novaOferta("T9", 2026, Semestre.SEGUNDO,
                new Disciplina("TEST", "Testes", 40));

        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular(" ", aluno));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular("MAT-010", null));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular(aluno));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.possuiMatriculaDe(null));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.possuiMatriculaComCodigo(" "));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.buscarMatricula(null));
    }

    @Test
    @DisplayName("Deve considerar o mesmo aluno pelo registro acadêmico ao impedir duplicidade")
    void deveImpedirDuplicidadePorIdentidadeDoAluno() {
        var aluno = new Aluno("RA010", "Helena", "helena@email.com");
        var mesmoAluno = new Aluno("ra010", "Helena Atualizada", "helena2@email.com");
        var oferta = novaOferta("T10", 2026, Semestre.SEGUNDO,
                new Disciplina("ARQ", "Arquitetura", 80));
        oferta.matricular("MAT-011", aluno);

        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular("MAT-012", mesmoAluno));
        assertEquals(1, oferta.getTotalMatriculados());
    }

    @Test
    @DisplayName("Matrícula cancelada continua pertencendo à oferta e impede duplicidade na mesma oferta")
    void deveManterHistoricoDaOfertaAposCancelamento() {
        var aluno = new Aluno("RA011", "Igor", "igor@email.com");
        var oferta = novaOferta("T11", 2026, Semestre.SEGUNDO,
                new Disciplina("REQ", "Requisitos", 80));
        var matricula = oferta.matricular("MAT-013", aluno);
        matricula.cancelar();

        assertTrue(oferta.possuiMatriculaDe(aluno));
        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular("MAT-014", aluno));
        assertEquals(1, oferta.getTotalMatriculados());
    }

    @Test
    @DisplayName("Oferta deve informar o mesmo período letivo da turma")
    void deveInformarPeriodoLetivoDaTurma() {
        var periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        var turma = new Turma("T12", periodo);
        var oferta = turma.ofertarDisciplina(new Disciplina("WEB", "Web", 80));

        assertSame(periodo, oferta.getPeriodoLetivo());
        assertSame(turma, oferta.getTurma());
        assertEquals("WEB", oferta.getDisciplina().getCodigo());
    }

    @Test
    @DisplayName("Sobrecarga sem código também deve validar aluno nulo")
    void deveValidarAlunoNaSobrecargaSemCodigo() {
        var oferta = novaOferta("T13", 2026, Semestre.SEGUNDO,
                new Disciplina("UX", "Experiência do Usuário", 40));

        assertThrows(IllegalArgumentException.class,
                () -> oferta.matricular((Aluno) null));
    }

    private OfertaDisciplina novaOferta(
            String codigoTurma,
            int ano,
            Semestre semestre,
            Disciplina disciplina
    ) {
        var turma = new Turma(codigoTurma, new PeriodoLetivo(ano, semestre));
        return turma.ofertarDisciplina(disciplina);
    }
}
