package br.edu.sistemaacademico.api;
import br.edu.sistemaacademico.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
@GetMapping
public ResponseEntity<?> listar() {
var aluno = new Aluno("25131800-2", "Luiz Henrique de Amorais Franco", "luiz@unicesumar.edu.br");
var periodo = new PeriodoLetivo(2026, Semestre.SEGUNDO);
var turma = new Turma("ESOF7AS-NA", periodo);
var disciplina = new Disciplina("POO", "Programacao Orientada a Objetos", 80);
var oferta = turma.ofertarDisciplina(disciplina);
var matricula = oferta.matricular("MAT-001", aluno);
return ResponseEntity.ok(List.of(Map.of("codigo", matricula.getCodigo(), "aluno", aluno.getNome(), "ra", "25131800-2", "turma", turma.getCodigo(), "disciplina", disciplina.getNome())));
}
@GetMapping("/info")
public ResponseEntity<?> info() {
return ResponseEntity.ok(Map.of("ra", "25131800-2", "nome", "Luiz Henrique de Amorais Franco", "curso", "Engenharia de Software"));
}
}
