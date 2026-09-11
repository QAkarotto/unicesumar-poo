package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncapsulamentoDominioTest {

    @Test
    @DisplayName("Lista de matrículas do aluno não deve permitir alteração externa")
    void deveProtegerMatriculasDoAluno() {
        var aluno = new Aluno("RA001", "Ana", "ana@email.com");
        var oferta = novaOferta("POO");
        oferta.matricular("MAT-001", aluno);

        var matriculas = aluno.getMatriculas();

        assertEquals(1, matriculas.size());
        assertThrows(UnsupportedOperationException.class, matriculas::clear);
        assertEquals(1, aluno.getMatriculas().size());
    }

    @Test
    @DisplayName("Lista de ofertas da turma não deve permitir alteração externa")
    void deveProtegerOfertasDaTurma() {
        var turma = new Turma("ESOFT4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma.ofertarDisciplina(new Disciplina("POO", "POO", 80));

        var ofertas = turma.getOfertas();

        assertEquals(1, ofertas.size());
        assertThrows(UnsupportedOperationException.class, ofertas::clear);
        assertEquals(1, turma.getOfertas().size());
    }

    @Test
    @DisplayName("Lista de matrículas da oferta não deve permitir alteração externa")
    void deveProtegerMatriculasDaOferta() {
        var aluno = new Aluno("RA002", "Bruno", "bruno@email.com");
        var oferta = novaOferta("BD");
        oferta.matricular("MAT-002", aluno);

        var matriculas = oferta.getMatriculas();

        assertEquals(1, matriculas.size());
        assertThrows(UnsupportedOperationException.class, matriculas::clear);
        assertEquals(1, oferta.getTotalMatriculados());
    }

    private OfertaDisciplina novaOferta(String codigoDisciplina) {
        var turma = new Turma("T-" + codigoDisciplina,
                new PeriodoLetivo(2026, Semestre.SEGUNDO));
        return turma.ofertarDisciplina(
                new Disciplina(codigoDisciplina, "Disciplina " + codigoDisciplina, 80)
        );
    }
}
