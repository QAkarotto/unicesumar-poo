package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// @RestController: esta classe recebe requisições HTTP e devolve o retorno em JSON
// @RequestMapping("/api"): todas as rotas começam com /api
@RestController
@RequestMapping("/api")
public class AcademicoController {

    // Objetos só para montar o JSON. Não devolvemos Aluno/Matricula direto porque
    // um aponta para o outro e o JSON ficaria num loop infinito.
    public record MatriculaResposta(String id, String raAluno, String nomeAluno,
                                    String disciplina, String resultado) {}

    public record AlunoResposta(String id, String nome, String email, int totalMatriculas) {}

    // Dados que chegam no JSON do POST
    public record NovaMatriculaRequisicao(String raAluno, String codigoTurma, String siglaDisciplina) {}

    // Dados em memória (nada de banco de dados nesta etapa)
    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();
    private final Map<String, OfertaDisciplina> ofertas = new HashMap<>();

    // O Spring cria uma única instância do controller, então estes dados
    // ficam vivos enquanto a aplicação estiver rodando.
    public AcademicoController() {
        var pedro = new Aluno("Pedro Henrique", "RA2026101", "pedro.henrique@email.com");
        var marina = new Aluno("Marina Costa", "RA2026102", "marina.costa@email.com");
        alunos.put(pedro.getId(), pedro);
        alunos.put(marina.getId(), marina);

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bd = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        var bd2026A = turma2026A.ofertarDisciplina(bd);
        var poo2026B = turma2026B.ofertarDisciplina(poo);

        // chave = código da turma + "/" + sigla da disciplina
        ofertas.put("ESOFT4S-NA/POO", poo2025);
        ofertas.put("ESOFT4S-NB/POO", poo2026A);
        ofertas.put("ESOFT4S-NB/BD", bd2026A);
        ofertas.put("ADSIS4S/POO", poo2026B);

        // Matrículas iniciais (usando as regras do domínio)
        var m1 = poo2025.matricular(pedro);
        m1.concluir(ResultadoAcademico.REPROVADO);
        matriculas.put("MAT-001", m1);

        var m2 = poo2026A.matricular(pedro);
        m2.concluir(ResultadoAcademico.APROVADO);
        matriculas.put("MAT-002", m2);

        var m3 = poo2026A.matricular(marina);
        matriculas.put("MAT-003", m3);
    }

    // GET /api/matriculas/MAT-001
    // @PathVariable pega o valor que está entre chaves na URL
    @GetMapping("/matriculas/{id}")
    public ResponseEntity<?> buscarMatricula(@PathVariable("id") String id) {
        Matricula matricula = matriculas.get(id);

        if (matricula == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)           // 404
                    .body(Map.of("erro", "Matrícula não encontrada: " + id));
        }
        return ResponseEntity.ok(paraResposta(id, matricula));            // 200
    }

    // GET /api/alunos/RA2026101
    @GetMapping("/alunos/{ra}")
    public ResponseEntity<?> buscarAluno(@PathVariable("ra") String ra) {
        Aluno aluno = alunos.get(ra);

        if (aluno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)           // 404
                    .body(Map.of("erro", "Aluno não encontrado: " + ra));
        }
        return ResponseEntity.ok(new AlunoResposta(                       // 200
                aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getMatriculas().size()));
    }

    // POST /api/matriculas
    // @RequestBody converte o JSON recebido no objeto NovaMatriculaRequisicao
    @PostMapping("/matriculas")
    public ResponseEntity<?> criarMatricula(@RequestBody NovaMatriculaRequisicao dados) {
        Aluno aluno = alunos.get(dados.raAluno());
        OfertaDisciplina oferta = ofertas.get(dados.codigoTurma() + "/" + dados.siglaDisciplina());

        if (aluno == null || oferta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)           // 404
                    .body(Map.of("erro", "Aluno ou oferta de disciplina não encontrados"));
        }

        try {
            // As regras de matrícula ficam no domínio (OfertaDisciplina.matricular),
            // o controller só chama o método e trata o resultado.
            Matricula nova = oferta.matricular(aluno);

            String id = "MAT-" + String.format("%03d", matriculas.size() + 1);
            matriculas.put(id, nova);

            return ResponseEntity.status(HttpStatus.CREATED)             // 201
                    .body(paraResposta(id, nova));
        } catch (IllegalStateException | IllegalArgumentException e) {
            // O domínio recusou a matrícula: devolvemos 409 com o motivo
            return ResponseEntity.status(HttpStatus.CONFLICT)            // 409
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    // Converte uma Matricula do domínio no objeto que vira JSON
    private MatriculaResposta paraResposta(String id, Matricula m) {
        return new MatriculaResposta(
                id,
                m.getAluno().getId(),
                m.getAluno().getNome(),
                m.getOferta().getDisciplina().getNome(),
                m.getResultado());
    }
}