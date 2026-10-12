package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final CadastroAcademico cadastro;

    public MatriculaController(CadastroAcademico cadastro) {
        this.cadastro = cadastro;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResposta> consultar(@PathVariable("id") String id) {
        Matricula matricula = cadastro.buscarMatricula(id);

        if (matricula == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MatriculaResposta.de(matricula));
    }
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody MatriculaRequisicao requisicao) {
        Aluno aluno = cadastro.buscarAluno(requisicao.registroAcademico());
        if (aluno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado.");
        }

        OfertaDisciplina oferta = cadastro.buscarOferta(requisicao.codigoTurma(), requisicao.codigoDisciplina());
        if (oferta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta não encontrada.");
        }

        try {
            Matricula matricula = cadastro.matricular(aluno, oferta);
            return ResponseEntity.status(HttpStatus.CREATED).body(MatriculaResposta.de(matricula));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}