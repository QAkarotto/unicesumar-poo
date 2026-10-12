
package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SistemaAcademicoService {

    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<Long, Matricula> matriculas = new HashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new HashMap<>();

    private long proximoIdMatricula = 1;

    public SistemaAcademicoService() {

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

        alunos.put(paola.getRa(), paola);
        alunos.put(bruno.getRa(), bruno);

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
        return alunos.get(ra);
    }

    public Matricula buscarMatricula(Long id) {
        return matriculas.get(id);
    }

    public Long criarMatricula(String ra, String codigoDisciplina) {

        Aluno aluno = alunos.get(ra);

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
