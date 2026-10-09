package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Guarda em memória os objetos do domínio usados pela API.
 * O Spring cria uma única instância e a injeta nos controllers,
 * então todos enxergam os mesmos alunos, turmas e matrículas.
 */
@Component
public class DadosAcademicos {
    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Turma> turmas = new LinkedHashMap<>();

    public DadosAcademicos() {
        var paola = adicionarAluno(new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com"));
        var bruno = adicionarAluno(new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com"));

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = adicionarTurma(new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO)));
        var turma2026A = adicionarTurma(new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO)));
        var turma2026B = adicionarTurma(new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO)));

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        poo2025.matricular(proximoCodigoMatricula(), paola).concluir(ResultadoAcademico.REPROVADO);
        poo2026A.matricular(proximoCodigoMatricula(), paola).concluir(ResultadoAcademico.APROVADO);
        poo2026A.matricular(proximoCodigoMatricula(), bruno);
    }

    public Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
    }

    public Optional<OfertaDisciplina> buscarOferta(String codigoTurma, String codigoDisciplina) {
        return Optional.ofNullable(turmas.get(codigoTurma))
                .flatMap(turma -> turma.getOfertas().stream()
                        .filter(oferta -> oferta.getDisciplina().getCodigo().equals(codigoDisciplina))
                        .findFirst());
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return todasAsMatriculas().stream()
                .filter(matricula -> matricula.getCodigo().equals(codigo))
                .findFirst();
    }

    /**
     * O código gerado por OfertaDisciplina.matricular(aluno) conta só as matrículas
     * daquela oferta, então se repetiria entre ofertas. Aqui contamos todas.
     */
    public String proximoCodigoMatricula() {
        return "MAT-%03d".formatted(todasAsMatriculas().size() + 1);
    }

    private List<Matricula> todasAsMatriculas() {
        return alunos.values().stream()
                .flatMap(aluno -> aluno.getMatriculas().stream())
                .toList();
    }

    private Aluno adicionarAluno(Aluno aluno) {
        alunos.put(aluno.getRegistroAcademico(), aluno);
        return aluno;
    }

    private Turma adicionarTurma(Turma turma) {
        turmas.put(turma.getCodigo(), turma);
        return turma;
    }
}
