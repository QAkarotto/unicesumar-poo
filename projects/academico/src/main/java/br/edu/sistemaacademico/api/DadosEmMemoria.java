package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Guarda os objetos do domínio em memória para os dois Controllers compartilharem
 * o mesmo estado (o Spring cria uma única instância deste @Component).
 * Não tem regra de negócio: as regras continuam em Aluno, Matricula,
 * OfertaDisciplina e Turma.
 */
@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos;
    private final Map<String, Turma> turmas;

    public DadosEmMemoria() {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        // O código gerado por OfertaDisciplina.matricular(aluno) é "MAT-" + posição na oferta,
        // então se repete entre ofertas. Para a API consultar por id, informamos códigos únicos.
        poo2025.matricular("MAT-001", paola).concluir(ResultadoAcademico.REPROVADO);
        poo2026A.matricular("MAT-002", paola).concluir(ResultadoAcademico.APROVADO);
        poo2026A.matricular("MAT-003", bruno);

        alunos = Map.of(paola.getRegistroAcademico(), paola, bruno.getRegistroAcademico(), bruno);
        turmas = Map.of(
                turma2025.getCodigo(), turma2025,
                turma2026A.getCodigo(), turma2026A,
                turma2026B.getCodigo(), turma2026B
        );
    }

    public Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
    }

    public Optional<Turma> buscarTurma(String codigo) {
        return Optional.ofNullable(turmas.get(codigo));
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return todasAsMatriculas().stream()
                .filter(matricula -> matricula.getCodigo().equals(codigo))
                .findFirst();
    }

    public String proximoCodigoMatricula() {
        return "MAT-%03d".formatted(todasAsMatriculas().size() + 1);
    }

    private List<Matricula> todasAsMatriculas() {
        return alunos.values().stream()
                .flatMap(aluno -> aluno.getMatriculas().stream())
                .toList();
    }
}
