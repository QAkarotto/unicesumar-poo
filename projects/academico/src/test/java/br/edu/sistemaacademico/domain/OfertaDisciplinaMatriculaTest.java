package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaDisciplinaMatriculaTest {

    private OfertaDisciplina novaOferta() {
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        return turma.ofertarDisciplina(new Disciplina("POO", "Programação", 80));
    }

    @Test
    @DisplayName("Deve gerar códigos sequenciais ao matricular sem informar o código")
    void deveGerarCodigosSequenciais() {
        var oferta = novaOferta();
        var ana = new Aluno("RA001", "Ana Souza", "ana@email.com");
        var joao = new Aluno("RA002", "João Lima", "joao@email.com");

        var primeira = oferta.matricular(ana);
        var segunda = oferta.matricular(joao);

        assertEquals("MAT-1", primeira.getCodigo());
        assertEquals("MAT-2", segunda.getCodigo());
        assertEquals(2, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("Não deve permitir matricular o mesmo aluno duas vezes na oferta")
    void naoDevePermitirMatriculaDuplicada() {
        var oferta = novaOferta();
        var ana = new Aluno("RA001", "Ana Souza", "ana@email.com");
        oferta.matricular(ana);

        assertThrows(
                IllegalArgumentException.class,
                () -> oferta.matricular(ana)
        );
        assertEquals(1, oferta.getMatriculas().size());
    }

    @Test
    @DisplayName("A lista de matrículas da oferta deve ser imutável")
    void listaDeMatriculasDeveSerImutavel() {
        var oferta = novaOferta();

        assertThrows(
                UnsupportedOperationException.class,
                () -> oferta.getMatriculas().add(null)
        );
    }

    @Test
    @DisplayName("toString deve conter a disciplina e a turma da oferta")
    void toStringDeveConterDisciplinaETurma() {
        var oferta = novaOferta();

        var texto = oferta.toString();

        assertTrue(texto.contains("POO"));
        assertTrue(texto.contains("ESOFT4S-NA"));
    }
}
