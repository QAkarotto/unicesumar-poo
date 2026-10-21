package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import br.edu.sistemaacademico.persistencia.AlunoRepository;
import br.edu.sistemaacademico.persistencia.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AcademicoController {
    private final DadosEmMemoria dados;
    private final AlunoRepository alunos;
    private final DisciplinaRepository disciplinas;

    public AcademicoController(DadosEmMemoria dados, AlunoRepository alunos, DisciplinaRepository disciplinas) {
        this.dados = dados;
        this.alunos = alunos;
        this.disciplinas = disciplinas;
    }

    @GetMapping("/alunos")
    public List<AlunoResponse> listarAlunos() {
        return alunos.listarTodos().stream().map(AlunoResponse::de).toList();
    }

    @GetMapping("/alunos/{registroAcademico}")
    public AlunoResponse buscarAluno(@PathVariable("registroAcademico") String registroAcademico) {
        return AlunoResponse.de(alunos.buscarPorRegistroAcademico(registroAcademico)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado.")));
    }

    @PostMapping("/alunos")
    public ResponseEntity<AlunoResponse> cadastrarAluno(@RequestBody CriarAlunoRequest request) {
        var aluno = new Aluno(request.registroAcademico(), request.nome(), request.email());
        alunos.salvar(aluno);
        return ResponseEntity.created(URI.create("/api/alunos/" + aluno.getRegistroAcademico()))
                .body(AlunoResponse.de(aluno));
    }

    @GetMapping("/disciplinas")
    public List<DisciplinaResponse> listarDisciplinas() {
        return disciplinas.listarTodas().stream().map(DisciplinaResponse::de).toList();
    }

    @GetMapping("/disciplinas/{codigo}")
    public DisciplinaResponse buscarDisciplina(@PathVariable("codigo") String codigo) {
        return DisciplinaResponse.de(disciplinas.buscarPorCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada.")));
    }

    @PostMapping("/disciplinas")
    public ResponseEntity<DisciplinaResponse> cadastrarDisciplina(@RequestBody CriarDisciplinaRequest request) {
        var disciplina = new Disciplina(request.codigo(), request.nome(), request.cargaHoraria());
        disciplinas.salvar(disciplina);
        return ResponseEntity.created(URI.create("/api/disciplinas/" + disciplina.getCodigo()))
                .body(DisciplinaResponse.de(disciplina));
    }

    @GetMapping("/matriculas/{codigo}")
    public MatriculaResponse buscarMatricula(@PathVariable("codigo") String codigo) {
        return MatriculaResponse.de(dados.buscarMatricula(codigo));
    }

    @PostMapping("/matriculas")
    public ResponseEntity<MatriculaResponse> matricular(@RequestBody CriarMatriculaRequest request) {
        if (request.registroAcademico() == null || request.registroAcademico().isBlank()
                || request.ofertaId() == null || request.ofertaId().isBlank()) {
            throw new IllegalArgumentException("Informe registroAcademico e ofertaId.");
        }
        var matricula = dados.matricular(request.registroAcademico().trim(), request.ofertaId().trim());
        return ResponseEntity.created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResponse.de(matricula));
    }

    public record CriarMatriculaRequest(String registroAcademico, String ofertaId) {}

    public record CriarAlunoRequest(String registroAcademico, String nome, String email) {}

    public record CriarDisciplinaRequest(String codigo, String nome, int cargaHoraria) {}

    public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {
        static DisciplinaResponse de(Disciplina disciplina) {
            return new DisciplinaResponse(disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
        }
    }

    public record AlunoResponse(String registroAcademico, String nome, String email) {
        static AlunoResponse de(Aluno aluno) {
            return new AlunoResponse(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail());
        }
    }

    public record MatriculaResponse(String codigo, AlunoResponse aluno, String codigoDisciplina,
                                    String codigoTurma, int ano, Semestre semestre,
                                    SituacaoMatricula situacao, ResultadoAcademico resultado) {
        static MatriculaResponse de(Matricula matricula) {
            var turma = matricula.getTurma();
            return new MatriculaResponse(matricula.getCodigo(), AlunoResponse.de(matricula.getAluno()),
                    matricula.getOfertaDisciplina().getDisciplina().getCodigo(), turma.getCodigo(),
                    turma.getPeriodoLetivo().getAno(), turma.getPeriodoLetivo().getSemestre(),
                    matricula.getSituacao(), matricula.getResultado());
        }
    }
}
