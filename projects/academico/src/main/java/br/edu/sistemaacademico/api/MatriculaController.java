package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final DadosAcademicos dados;

    public MatriculaController(DadosAcademicos dados) {
        this.dados = dados;
    }

    // GET /api/matriculas/MAT-001
    @GetMapping("/{id}")
    public ResponseEntity<?> consultar(@PathVariable("id") String id) {
        Matricula matricula = dados.buscarMatricula(id);

        if (matricula == null) {
            return ErroResponse.de(HttpStatus.NOT_FOUND,
                    "Matrícula não encontrada: " + id); // 404
        }

        return ResponseEntity.ok(MatriculaResponse.de(matricula)); // 200
    }

    // POST /api/matriculas
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarMatriculaRequest requisicao) {
        // O JSON veio completo
        if (!requisicao.temTodosOsCampos()) {
            return ErroResponse.de(HttpStatus.BAD_REQUEST,
                    "Informe registroAcademico, codigoTurma e codigoDisciplina.");
        } //400

        String registroAcademico = requisicao.registroAcademico().trim();
        String codigoTurma = requisicao.codigoTurma().trim();
        String codigoDisciplina = requisicao.codigoDisciplina().trim();

        // O aluno existe
        Aluno aluno = dados.buscarAluno(registroAcademico);
        if (aluno == null) {
            return ErroResponse.de(HttpStatus.NOT_FOUND,
                    "Aluno não encontrado: " + registroAcademico);
        } //404

        // turma oferta essa disciplina
        OfertaDisciplina oferta = dados.buscarOferta(codigoTurma, codigoDisciplina);
        if (oferta == null) {
            return ErroResponse.de(HttpStatus.NOT_FOUND,
                    "A turma " + codigoTurma + " não oferta a disciplina " + codigoDisciplina + ".");
        } //404

        //Matricular: as regras ficam no domínio. Se alguma for violada, ele lança uma exceção.
        try {
            Matricula matricula = dados.matricular(aluno, oferta);
            URI local = URI.create("/api/matriculas/" + matricula.getCodigo());
            return ResponseEntity.created(local).body(MatriculaResponse.de(matricula)); // 201
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ErroResponse.de(HttpStatus.CONFLICT, e.getMessage()); // 409
        }
    }
}
