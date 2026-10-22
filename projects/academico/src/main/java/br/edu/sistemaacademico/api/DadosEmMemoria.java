package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DadosEmMemoria {
    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();
    private final AtomicInteger contador = new AtomicInteger();

    public DadosEmMemoria() {
        alunos.put("RA-001", new Aluno("RA-001", "Maria Silva", "maria@email.com"));
        alunos.put("RA-002", new Aluno("RA-002", "João Souza", "joao@email.com"));

        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        Turma turma = new Turma("T1", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        OfertaDisciplina oferta = turma.ofertarDisciplina(poo);
        ofertas.put("POO", oferta);

        matricular("RA-001", "POO");
    }

    public Optional<Aluno> buscarAluno(String ra) {
        return Optional.ofNullable(alunos.get(ra));
    }

    public Optional<Matricula> buscarMatricula(String id) {
        return Optional.ofNullable(matriculas.get(id));
    }

    public Optional<OfertaDisciplina> buscarOferta(String codigo) {
        return Optional.ofNullable(ofertas.get(codigo));
    }

    public Matricula matricular(String ra, String codigoDisciplina) {
        Aluno aluno = alunos.get(ra);
        OfertaDisciplina oferta = ofertas.get(codigoDisciplina);
        String id = String.format("MAT-%03d", contador.incrementAndGet());
        Matricula matricula = oferta.matricular(aluno, id);
        matriculas.put(id, matricula);
        return matricula;
    }
}