package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegrasAdicionaisTest {

    @Test
    void naoDevePermitirAlterarEmailParaValorInvalido() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );
    }

    @Test
    void naoDeveConcluirMatriculaSemResultado() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();

        // Ação e verificação do resultado
        assertThrows(
                IllegalArgumentException.class,
                () -> matricula.concluir(null)
        );

        assertEquals(SituacaoMatricula.ATIVA, matricula.getSituacao());
    }

    @Test
    void naoDevePermitirCancelarMatriculaJaTrancada() {

        // Preparação
        Matricula matricula = criarMatriculaEmAndamento();
        matricula.trancar();

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                matricula::cancelar
        );
    }

    @Test
    void deveCriarMatriculaPelaTurmaQuandoExisteUmaUnicaOferta() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        // Ação
        Matricula matricula = new Matricula(
                "MAT-001",
                aluno,
                turma
        );

        // Verificação do resultado
        assertEquals(oferta, matricula.getOfertaDisciplina());
    }

    @Test
    void naoDeveCriarMatriculaPelaTurmaComMaisDeUmaOferta() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        turma.ofertarDisciplina(
                new Disciplina(
                        "BD",
                        "Banco de Dados",
                        80
                )
        );

        // Ação e verificação do resultado
        assertThrows(
                IllegalStateException.class,
                () -> new Matricula("MAT-001", aluno, turma)
        );
    }

    @Test
    void naoDevePermitirAlterarAsListasInternasDoDominio() {

        // Preparação
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        Matricula matricula = oferta.matricular("MAT-001", aluno);

        // Ação e verificação do resultado
        assertThrows(
                UnsupportedOperationException.class,
                () -> turma.getOfertas().add(oferta)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().add(matricula)
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().add(matricula)
        );
    }

    private Matricula criarMatriculaEmAndamento() {

        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        Turma turma = new Turma(
                "ESOFT4S-NA",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        OfertaDisciplina oferta = turma.ofertarDisciplina(
                new Disciplina(
                        "POO",
                        "Programação Orientada a Objetos",
                        80
                )
        );

        return oferta.matricular("MAT-001", aluno);
    }
}