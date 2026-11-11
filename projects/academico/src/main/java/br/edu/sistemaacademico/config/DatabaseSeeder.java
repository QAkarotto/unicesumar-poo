package br.edu.sistemaacademico.config;

import br.edu.sistemaacademico.domain.*;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.MatriculaRepository;
import br.edu.sistemaacademico.repository.OfertaDisciplinaRepository;
import br.edu.sistemaacademico.repository.TurmaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TurmaRepository turmaRepository;
    private final OfertaDisciplinaRepository ofertaRepository;
    private final MatriculaRepository matriculaRepository;

    public DatabaseSeeder(
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository,
            TurmaRepository turmaRepository,
            OfertaDisciplinaRepository ofertaRepository,
            MatriculaRepository matriculaRepository) {
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.turmaRepository = turmaRepository;
        this.ofertaRepository = ofertaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    public void run(String... args) {
        if (alunoRepository.count() > 0) {
            return;
        }

        // 1. Alunos iniciais
        var aluno1 = new Aluno("RA2026001", "Aluno Exemplo Um", "aluno1@example.com");
        var aluno2 = new Aluno("RA2026002", "Aluno Exemplo Dois", "aluno2@example.com");
        var aluno3 = new Aluno("RA2026003", "Carlos Eduardo Silva", "carlos.silva@example.com");
        var aluno4 = new Aluno("RA2026004", "Mariana Santos", "mariana.santos@example.com");
        alunoRepository.save(aluno1);
        alunoRepository.save(aluno2);
        alunoRepository.save(aluno3);
        alunoRepository.save(aluno4);

        // 2. Turma inicial
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmaRepository.save(turma);

        // 3. Disciplinas iniciais
        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD", "Banco de Dados", 80);
        var web = new Disciplina("WEB", "Desenvolvimento Web com React", 60);
        var es = new Disciplina("ES", "Engenharia de Software", 80);
        disciplinaRepository.save(poo);
        disciplinaRepository.save(bd);
        disciplinaRepository.save(web);
        disciplinaRepository.save(es);

        // 4. Ofertas de Disciplinas
        var ofertaPoo = new OfertaDisciplina("POO-2026-2", turma, poo);
        var ofertaBd = new OfertaDisciplina("BD-2026-2", turma, bd);
        var ofertaWeb = new OfertaDisciplina("WEB-2026-2", turma, web);
        ofertaRepository.save(ofertaPoo);
        ofertaRepository.save(ofertaBd);
        ofertaRepository.save(ofertaWeb);

        // 5. Matrícula inicial
        var matricula1 = new Matricula("MAT-001", aluno1, ofertaPoo);
        matriculaRepository.save(matricula1);
    }
}
