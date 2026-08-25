package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;

public class SistemaAcademico {

    public static void main(String[] args) {

        var paola = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );
        System.out.println("Programa executado com sucesso!");
    }
}