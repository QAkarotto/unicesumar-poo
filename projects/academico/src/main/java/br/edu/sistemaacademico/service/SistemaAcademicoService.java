
package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SistemaAcademicoService {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    private final Map<Long, Matricula> matriculas = new HashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new HashMap<>();

    private long proximoIdMatricula = 1;

    public SistemaAcademicoService(
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository) {

        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "ESOFT4S-NB",
                periodo
        );

        OfertaDisciplina ofertaPoo = turma.ofertarDisciplina(poo);

        ofertas.put("POO", ofertaPoo);
    }

    public Aluno buscarAluno(String ra) {
        return alunoRepository.buscarPorRa(ra).orElse(null);
    }

    public Aluno salvarAluno(Aluno aluno) {
        return alunoRepository.salvar(aluno);
    }

    public Disciplina buscarDisciplina(String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo).orElse(null);
    }

    public Disciplina salvarDisciplina(Disciplina disciplina) {
        return disciplinaRepository.salvar(disciplina);
    }

    public Matricula buscarMatricula(Long id) {
        return matriculas.get(id);
    }

    public Long criarMatricula(String ra, String codigoDisciplina) {

        Aluno aluno = buscarAluno(ra);

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não encontrado.");
        }

        OfertaDisciplina oferta = ofertas.get(codigoDisciplina);

        if (oferta == null) {
            throw new IllegalArgumentException("Disciplina não encontrada.");
        }

        Matricula matricula = oferta.matricular(aluno);

        Long id = proximoIdMatricula++;

        matriculas.put(id, matricula);

        return id;
    }
}
