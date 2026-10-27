package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> buscar(@PathVariable String id) {
        Matricula matricula = PonteApi.matricula(id);
        if (matricula == null) {
            return ResponseEntity.status(404).body(Map.of("mensagem", "Matrícula não encontrada."));
        }
        return ResponseEntity.ok(corpo(matricula));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> registrar(@RequestBody Map<String, String> pedido) {
        try {
            Matricula matricula = PonteApi.matricular(
                    pedido.get("ra"),
                    pedido.get("turma"),
                    pedido.get("disciplina"));
            if (matricula == null) {
                return ResponseEntity.status(404).body(Map.of("mensagem", "Aluno ou oferta não encontrado."));
            }
            return ResponseEntity.status(201).body(corpo(matricula));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("mensagem", e.getMessage()));
        }
    }

    private Map<String, String> corpo(Matricula matricula) {
        return Map.of(
                "codigo", matricula.getCodigo(),
                "ra", matricula.getAluno().getIdentificadorAcademico(),
                "nome", matricula.getAluno().getNome(),
                "turma", matricula.getOfertaDisciplina().getTurma().getCodigo(),
                "disciplina", matricula.getOfertaDisciplina().getDisciplina().getCodigo(),
                "situacao", matricula.getSituacao());
    }

    static class PonteApi {

        private static final Map<String, Aluno> alunos = new LinkedHashMap<>();
        private static final Map<String, Matricula> matriculas = new LinkedHashMap<>();
        private static final Map<String, Turma> turmas = new LinkedHashMap<>();
        private static int seq = 11;

        static {
            Turma turma = new Turma("TI-2026-1", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
            turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 60));
            turmas.put(turma.getCodigo(), turma);
            Aluno helena = new Aluno("RA2401", "Helena Prado", "helena.prado@exemplo.edu");
            alunos.put(helena.getIdentificadorAcademico(), helena);
            Matricula matriculaInicial = turma.buscarOferta("BD").matricular("MAT-001", helena);
            matriculas.put(matriculaInicial.getCodigo(), matriculaInicial);
            alunos.put("RA2403", new Aluno("RA2403", "Lia Ferreira", "lia.ferreira@exemplo.edu"));
        }

        static Aluno aluno(String ra) {
            return alunos.get(ra);
        }

        static Matricula matricula(String codigo) {
            return matriculas.get(codigo);
        }

        static Matricula matricular(String ra, String codigoTurma, String codigoDisciplina) {
            if (vazio(ra) || vazio(codigoTurma) || vazio(codigoDisciplina)) {
                throw new IllegalArgumentException("Informe o RA, a turma e a disciplina.");
            }
            Aluno aluno = alunos.get(ra);
            Turma turma = turmas.get(codigoTurma);
            if (aluno == null || turma == null) {
                return null;
            }
            OfertaDisciplina oferta = turma.buscarOferta(codigoDisciplina);
            if (oferta == null) {
                return null;
            }
            Matricula matricula = oferta.matricular("MAT-" + seq++, aluno);
            matriculas.put(matricula.getCodigo(), matricula);
            return matricula;
        }

        private static boolean vazio(String valor) {
            return valor == null || valor.isBlank();
        }
    }
}
