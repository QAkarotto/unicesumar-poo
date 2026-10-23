package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.*;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class AcademicoController {

    private final DadosEmMemoria dados;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public AcademicoController(DadosEmMemoria dados,
                              AlunoRepository alunoRepository,
                              DisciplinaRepository disciplinaRepository) {
        this.dados = dados;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping("/alunos/{registroAcademico}")
    public AlunoResponse buscarAluno(@PathVariable("registroAcademico") String registroAcademico) {
        try {
            return AlunoResponse.de(dados.buscarAluno(registroAcademico));
        } catch (RecursoNaoEncontradoException e) {
            return alunoRepository.buscarPorRegistroAcademico(registroAcademico)
                    .map(AlunoResponse::de)
                    .orElseThrow(() -> e);
        }
    }

    @PostMapping("/alunos")
    public ResponseEntity<AlunoResponse> cadastrarAluno(@RequestBody CriarAlunoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Corpo da requisição é obrigatório.");
        }
        String ra = request.obterRa();
        if (ra == null || ra.isBlank()) {
            throw new IllegalArgumentException("O registro acadêmico é obrigatório.");
        }
        if (alunoRepository.existePorRa(ra) || dados.temAluno(ra)) {
            throw new IllegalStateException("Já existe um aluno cadastrado com o registro acadêmico informado.");
        }

        var aluno = new Aluno(ra, request.nome(), request.email());
        var salvo = alunoRepository.salvar(aluno);
        dados.registrarAluno(salvo);

        return ResponseEntity.created(URI.create("/api/alunos/" + salvo.getRegistroAcademico()))
                .body(AlunoResponse.de(salvo));
    }

    @GetMapping("/disciplinas/{codigo}")
    public DisciplinaResponse buscarDisciplina(@PathVariable("codigo") String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }

    @PostMapping("/disciplinas")
    public ResponseEntity<DisciplinaResponse> cadastrarDisciplina(@RequestBody CriarDisciplinaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Corpo da requisição é obrigatório.");
        }
        if (request.codigo() == null || request.codigo().isBlank()) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        if (disciplinaRepository.existePorCodigo(request.codigo().trim())) {
            throw new IllegalStateException("Já existe uma disciplina com o código informado.");
        }

        Integer carga = request.obterCargaHoraria();
        if (carga == null) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }

        var disciplina = new Disciplina(request.codigo(), request.nome(), carga);
        var salva = disciplinaRepository.salvar(disciplina);

        return ResponseEntity.created(URI.create("/api/disciplinas/" + salva.getCodigo()))
                .body(DisciplinaResponse.de(salva));
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
        String ra = request.registroAcademico().trim();
        if (!dados.temAluno(ra)) {
            alunoRepository.buscarPorRegistroAcademico(ra).ifPresent(dados::registrarAluno);
        }

        var matricula = dados.matricular(ra, request.ofertaId().trim());
        return ResponseEntity.created(URI.create("/api/matriculas/" + matricula.getCodigo()))
                .body(MatriculaResponse.de(matricula));
    }

    public record CriarAlunoRequest(String registroAcademico, String ra, String nome, String email) {
        public String obterRa() {
            if (registroAcademico != null && !registroAcademico.isBlank()) {
                return registroAcademico.trim();
            }
            if (ra != null && !ra.isBlank()) {
                return ra.trim();
            }
            return null;
        }
    }

    public record CriarDisciplinaRequest(String codigo, String nome, Integer cargaHoraria, Integer carga_horaria) {
        public Integer obterCargaHoraria() {
            return cargaHoraria != null ? cargaHoraria : carga_horaria;
        }
    }

    public record AlunoResponse(String registroAcademico, String nome, String email) {
        public static AlunoResponse de(Aluno aluno) {
            return new AlunoResponse(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail());
        }
    }

    public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {
        public static DisciplinaResponse de(Disciplina disciplina) {
            return new DisciplinaResponse(disciplina.getCodigo(), disciplina.getNome(), disciplina.getCargaHoraria());
        }
    }

    public record CriarMatriculaRequest(String registroAcademico, String ofertaId) {}

    public record MatriculaResponse(String codigo, AlunoResponse aluno, String codigoDisciplina,
                                    String codigoTurma, int ano, Semestre semestre,
                                    SituacaoMatricula situacao, ResultadoAcademico resultado) {
        public static MatriculaResponse de(Matricula matricula) {
            var turma = matricula.getTurma();
            return new MatriculaResponse(matricula.getCodigo(), AlunoResponse.de(matricula.getAluno()),
                    matricula.getOfertaDisciplina().getDisciplina().getCodigo(), turma.getCodigo(),
                    turma.getPeriodoLetivo().getAno(), turma.getPeriodoLetivo().getSemestre(),
                    matricula.getSituacao(), matricula.getResultado());
        }
    }
}
