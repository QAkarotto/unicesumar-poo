package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Turmas e matrículas continuam em memória (fora do escopo da Atividade 03).
 * Aluno e Disciplina agora vêm do banco, via Repository.
 */
@Component
public class DadosEmMemoria {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    // Alunos já carregados: mantém o MESMO objeto (e suas matrículas) entre requisições.
    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Turma> turmas = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();

    public DadosEmMemoria(AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository) {
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;

        var paola = buscarAluno("RA2026001");
        var bruno = buscarAluno("RA2026002");
        if (paola == null || bruno == null) {
            throw new IllegalStateException("Alunos iniciais não encontrados. Rode database/dados-iniciais.sql.");
        }

        var poo = carregarDisciplina("POO");
        var bancoDados = carregarDisciplina("BD");

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmas.put(turma2025.getCodigo(), turma2025);
        turmas.put(turma2026A.getCodigo(), turma2026A);
        turmas.put(turma2026B.getCodigo(), turma2026B);

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        var primeira = poo2025.matricular(paola);
        primeira.concluir(ResultadoAcademico.REPROVADO);
        registrar(primeira);

        var segunda = poo2026A.matricular(paola);
        segunda.concluir(ResultadoAcademico.APROVADO);
        registrar(segunda);

        registrar(poo2026A.matricular(bruno));
    }

    public Aluno buscarAluno(String ra) {
        Aluno emMemoria = alunos.get(ra);
        if (emMemoria != null) {
            return emMemoria;
        }
        Aluno doBanco = alunoRepository.buscarPorRa(ra).orElse(null);
        if (doBanco != null) {
            alunos.put(ra, doBanco);
        }
        return doBanco;
    }

    public Turma buscarTurma(String codigo) {
        return turmas.get(codigo);
    }

    public Matricula buscarMatricula(String id) {
        return matriculas.get(id);
    }

    public String registrar(Matricula matricula) {
        String id = String.format("MAT-%03d", matriculas.size() + 1);
        matriculas.put(id, matricula);
        return id;
    }

    private Disciplina carregarDisciplina(String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalStateException(
                        "Disciplina inicial não encontrada: " + codigo));
    }
}
