package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import br.edu.sistemaacademico.persistencia.AlunoRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DadosEmMemoria {
    private final AlunoRepository repositorioAlunos;
    // matriculas e ofertas continuam so em memoria. o aluno carregado do banco fica guardado aqui
    // pra que as matriculas dele (e a regra de aprovacao anterior) usem sempre o mesmo objeto
    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();
    private int proximoCodigo = 2;

    public DadosEmMemoria(AlunoRepository repositorioAlunos) {
        this.repositorioAlunos = repositorioAlunos;
        var aluno = new Aluno("RA2026001", "Aluno Exemplo Um", "aluno1@example.com");
        var outro = new Aluno("RA2026002", "Aluno Exemplo Dois", "aluno2@example.com");
        alunos.put(aluno.getRegistroAcademico(), aluno);
        alunos.put(outro.getRegistroAcademico(), outro);
        var turma = new Turma("ESOFT4S-NA", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        var poo = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        ofertas.put("POO-2026-2", poo);
        ofertas.put("BD-2026-2", turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80)));
        matriculas.put("MAT-001", poo.matricular("MAT-001", aluno));
    }

    public synchronized Aluno buscarAluno(String registroAcademico) {
        var aluno = alunos.get(registroAcademico);
        if (aluno == null) {
            aluno = exigirExistente(repositorioAlunos.buscarPorRa(registroAcademico).orElse(null),
                    "Aluno não encontrado.");
            alunos.put(registroAcademico, aluno);
        }
        return aluno;
    }

    public synchronized Matricula buscarMatricula(String codigo) {
        return exigirExistente(matriculas.get(codigo), "Matrícula não encontrada.");
    }

    public synchronized Matricula matricular(String registroAcademico, String ofertaId) {
        var aluno = buscarAluno(registroAcademico);
        var oferta = exigirExistente(ofertas.get(ofertaId), "Oferta não encontrada.");
        var codigo = "MAT-%03d".formatted(proximoCodigo);
        var matricula = oferta.matricular(codigo, aluno);
        matriculas.put(codigo, matricula);
        proximoCodigo++;
        return matricula;
    }

    private static <T> T exigirExistente(T objeto, String mensagem) {
        if (objeto == null) {
            throw new RecursoNaoEncontradoException(mensagem);
        }
        return objeto;
    }
}
