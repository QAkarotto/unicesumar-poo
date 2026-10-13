
package br.edu.sistemaacademico;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class DadosIniciaisConfig {

    @Bean
    public Map<String, Aluno> alunos() {
        Map<String, Aluno> alunos = new LinkedHashMap<>();

        Aluno paola = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );

        Aluno bruno = new Aluno(
                "RA2026002",
                "Bruno Santos",
                "bruno.santos@email.com"
        );

        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);

        return alunos;
    }

    @Bean
    public OfertaDisciplina ofertaDisciplina() {
        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        Turma turma = new Turma("ADSIS4S", periodo);

        return turma.ofertarDisciplina(poo);
    }

    @Bean
    public Map<String, Matricula> matriculas(
            Map<String, Aluno> alunos,
            OfertaDisciplina ofertaDisciplina) {

        Map<String, Matricula> matriculas = new LinkedHashMap<>();

        Matricula matricula = ofertaDisciplina.matricular(
                "MAT-001",
                alunos.get("RA2026001")
        );

        matriculas.put(matricula.getCodigo(), matricula);

        return matriculas;
    }
}
