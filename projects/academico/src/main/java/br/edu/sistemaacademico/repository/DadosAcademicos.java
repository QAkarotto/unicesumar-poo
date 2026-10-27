
package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DadosAcademicos {

    private final AlunoRepository alunoRepository;

    private final Map<String, Matricula> matriculas =
            new HashMap<>();

    private final Map<String, OfertaDisciplina> ofertas =
            new HashMap<>();

    public DadosAcademicos(AlunoRepository alunoRepository) {

        this.alunoRepository = alunoRepository;

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.SEGUNDO
        );

        Turma turma = new Turma(
                "ADSIS4S",
                periodo
        );

        OfertaDisciplina ofertaPoo =
                turma.ofertarDisciplina(poo);

        ofertas.put("POO-ADSIS4S", ofertaPoo);
    }

    public Optional<Aluno> buscarAluno(String ra) {
        return alunoRepository.buscarPorRa(ra);
    }

    public Optional<Matricula> buscarMatricula(
            String codigo) {

        return Optional.ofNullable(
                matriculas.get(codigo));
    }

    public Optional<OfertaDisciplina> buscarOferta(
            String codigo) {

        return Optional.ofNullable(
                ofertas.get(codigo));
    }

    public Matricula cadastrarMatricula(
            Aluno aluno,
            OfertaDisciplina oferta) {

        String codigo = "MAT-" + UUID.randomUUID();

        Matricula matricula =
                oferta.matricular(codigo, aluno);

        matriculas.put(codigo, matricula);

        return matricula;
    }
}
