package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;

public class SistemaAcademico {

    public static void main(String[] args) {
        System.out.println("=== Sistema Acadêmico ===");

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD", "Banco de Dados", 80);
        var so = new Disciplina("SO", "Sistemas Operacionais", 60);
        var er = new Disciplina("ER", "Engenharia de Requisitos", 40);

        var periodo20262 = new PeriodoLetivo(2026, Semestre.SEGUNDO);
        var turma = new Turma("ESOFT4S-NA", periodo20262);

        var ofertaPoo = turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bd);
        turma.ofertarDisciplina(so);
        turma.ofertarDisciplina(er);
        System.out.println("Turma: " + turma);

        try {
            turma.ofertarDisciplina(poo);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado (oferta duplicada): " + e.getMessage());
        }

        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Costa", "bruno.costa@email.com");

        var matriculaPaola = ofertaPoo.matricular("MAT-001", paola);
        var matriculaBruno = ofertaPoo.matricular("MAT-002", bruno);
        System.out.println("Matrícula: " + matriculaPaola);
        System.out.println("Matrícula: " + matriculaBruno);

        System.out.println("Matrículas em " + poo.getNome() + ": " + ofertaPoo.getMatriculas());

        try {
            ofertaPoo.matricular("MAT-003", paola);
        } catch (IllegalStateException e) {
            System.out.println("Erro esperado (matrícula duplicada): " + e.getMessage());
        }

        matriculaBruno.registrarResultado(ResultadoAcademico.REPROVADO);
        System.out.println("Resultado registrado: " + matriculaBruno);

        var periodo20271 = new PeriodoLetivo(2027, Semestre.PRIMEIRO);
        var turmaRecuperacao = new Turma("ESOFT5S-NA", periodo20271);
        var ofertaPooRecuperacao = turmaRecuperacao.ofertarDisciplina(poo);
        var novaMatriculaBruno = ofertaPooRecuperacao.matricular("MAT-004", bruno);
        System.out.println("Nova matrícula após reprovação: " + novaMatriculaBruno);

        matriculaPaola.registrarResultado(ResultadoAcademico.APROVADO);
        novaMatriculaBruno.registrarResultado(ResultadoAcademico.APROVADO);
        System.out.println("Resultado registrado: " + matriculaPaola);
        System.out.println("Resultado registrado: " + novaMatriculaBruno);

        var turmaOutra = new Turma("ESOFT6S-NA", new PeriodoLetivo(2027, Semestre.SEGUNDO));
        var ofertaPooOutra = turmaOutra.ofertarDisciplina(poo);
        try {
            ofertaPooOutra.matricular("MAT-005", paola);
        } catch (IllegalStateException e) {
            System.out.println("Erro esperado (já aprovado na disciplina): " + e.getMessage());
        }

        try {
            ofertaPooRecuperacao.matricular("MAT-006", bruno);
        } catch (IllegalStateException e) {
            System.out.println("Erro esperado (matrícula duplicada): " + e.getMessage());
        }

        System.out.println("Histórico de " + paola.getNome() + ": " + paola.getHistorico());
        System.out.println("Histórico de " + bruno.getNome() + ": " + bruno.getHistorico());

        paola.setEmail("paola.oliveira@universidade.edu.br");
        System.out.println("E-mail atualizado: " + paola.getEmail());
    }
}
