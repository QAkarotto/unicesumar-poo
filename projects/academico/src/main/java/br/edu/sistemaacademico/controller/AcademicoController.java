package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AcademicoController {

    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new LinkedHashMap<>();

    public record AlunoResposta(String ra, String nome, String email, int totalMatriculas) { }

    public record MatriculaResposta(String codigo, String ra, String aluno,
                                    String disciplina, String situacao, String resultado) { }

    public record NovaMatriculaRequisicao(String codigo, String ra, String oferta) { }

    public AcademicoController() {
        Aluno paola = new Aluno("RA2026001", "Paola Oliveira", "paola@email.com");
        Aluno bruno = new Aluno("RA2026002", "Bruno Santos", "bruno@email.com");
        Aluno carlos = new Aluno("RA2026003", "Carlos Silva", "carlos@email.com");
        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);
        alunos.put(carlos.getRegistroAcademico(), carlos);

        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        PeriodoLetivo periodo2026_1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma2026A = new Turma("ESOFT4S-NB", periodo2026_1);
        OfertaDisciplina poo2026A = turma2026A.ofertarDisciplina(poo);
        ofertas.put("POO-2026A", poo2026A);

        Matricula mat001 = new Matricula("MAT-001", paola, poo2026A);
        matriculas.put(mat001.getCodigo().toUpperCase(), mat001);
    }

    // GET /api/alunos/{identificador}
    @GetMapping("/alunos/{identificador}")
    public ResponseEntity<?> buscarAluno(@PathVariable String identificador) {
        Aluno a = alunos.get(identificador.toUpperCase());
        if (a == null) {
            return erro(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + identificador);
        }
        return ResponseEntity.ok(new AlunoResposta(
                a.getRegistroAcademico(), a.getNome(), a.getEmail(), a.getMatriculas().size()));
    }

    // GET /api/matriculas/{id}
    @GetMapping("/matriculas/{id}")
    public ResponseEntity<?> buscarMatricula(@PathVariable String id) {
        Matricula m = matriculas.get(id.toUpperCase());
        if (m == null) {
            return erro(HttpStatus.NOT_FOUND, "Matrícula não encontrada: " + id);
        }
        return ResponseEntity.ok(paraResposta(m));
    }

    // POST /api/matriculas   {"codigo":"MAT-010","ra":"RA2026002","oferta":"POO-2026A"}
    @PostMapping("/matriculas")
    public ResponseEntity<?> criarMatricula(@RequestBody NovaMatriculaRequisicao dados) {
        if (dados == null || dados.codigo() == null || dados.ra() == null || dados.oferta() == null) {
            return erro(HttpStatus.BAD_REQUEST, "Informe codigo, ra e oferta.");
        }

        Aluno aluno = alunos.get(dados.ra().toUpperCase());
        if (aluno == null) {
            return erro(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + dados.ra());
        }
        OfertaDisciplina oferta = ofertas.get(dados.oferta().toUpperCase());
        if (oferta == null) {
            return erro(HttpStatus.NOT_FOUND, "Oferta não encontrada: " + dados.oferta());
        }
        if (matriculas.containsKey(dados.codigo().toUpperCase())) {
            return erro(HttpStatus.CONFLICT, "Já existe matrícula com o código " + dados.codigo());
        }

        try {
        
            Matricula nova = new Matricula(dados.codigo(), aluno, oferta);
            matriculas.put(nova.getCodigo().toUpperCase(), nova);
            return ResponseEntity.status(HttpStatus.CREATED).body(paraResposta(nova));
        } catch (IllegalStateException | IllegalArgumentException e) {
           
            return erro(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    private MatriculaResposta paraResposta(Matricula m) {
        return new MatriculaResposta(
                m.getCodigo(),
                m.getAluno().getRegistroAcademico(),
                m.getAluno().getNome(),
                m.getOfertaDisciplina().getDisciplina().getCodigo(),
                m.getSituacao().toString(),
                m.getResultado() == null ? null : m.getResultado().toString());
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }
}
