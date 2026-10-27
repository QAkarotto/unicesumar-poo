package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SistemaAcademicoController {
    private final AlunoRepository alunos;
    private final DisciplinaRepository disciplinas;

    public SistemaAcademicoController(AlunoRepository alunos, DisciplinaRepository disciplinas) {
        this.alunos = alunos;
        this.disciplinas = disciplinas;
    }

    @PostMapping("/alunos")
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponse cadastrarAluno(@Valid @RequestBody AlunoRequest request) {
        return AlunoResponse.from(alunos.salvar(
                new Aluno(request.ra(), request.nome(), request.email())));
    }

    @GetMapping("/alunos/{ra}")
    public AlunoResponse consultarAluno(@PathVariable String ra) {
        return alunos.buscarPorRa(ra)
                .map(AlunoResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + ra));
    }

    @PostMapping("/disciplinas")
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplinaResponse cadastrarDisciplina(@Valid @RequestBody DisciplinaRequest request) {
        return DisciplinaResponse.from(disciplinas.salvar(
                new Disciplina(request.codigo(), request.nome(), request.cargaHoraria())));
    }

    @GetMapping("/disciplinas/{codigo}")
    public DisciplinaResponse consultarDisciplina(@PathVariable String codigo) {
        return disciplinas.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada: " + codigo));
    }

    public record AlunoRequest(
            @NotBlank String ra,
            @NotBlank String nome,
            @NotBlank @Email String email) {}

    public record DisciplinaRequest(
            @NotBlank String codigo,
            @NotBlank String nome,
            @Min(1) int cargaHoraria) {}

    public record AlunoResponse(String ra, String nome, String email) {
        static AlunoResponse from(Aluno aluno) {
            return new AlunoResponse(aluno.getRa(), aluno.getNome(), aluno.getEmail());
        }
    }

    public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {
        static DisciplinaResponse from(Disciplina disciplina) {
            return new DisciplinaResponse(
                    disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
        }
    }
}
