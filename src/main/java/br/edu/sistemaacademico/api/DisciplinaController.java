package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {
 private final DisciplinaRepository repository;
 public DisciplinaController(DisciplinaRepository repository) { this.repository = repository; }
 @PostMapping public ResponseEntity<?> criar(@RequestBody(required=false) CriarDisciplinaRequest request) {
  if (request == null || vazio(request.codigo()) || vazio(request.nome()) || request.cargaHoraria() <= 0) return ResponseEntity.badRequest().body("Informe código, nome e carga horária positiva.");
  try { Disciplina d = new Disciplina(request.codigo(), request.nome(), request.cargaHoraria()); repository.salvar(d);
   return ResponseEntity.created(URI.create("/api/disciplinas/" + d.getCodigo())).body(DisciplinaResponse.from(d));
  } catch (IllegalArgumentException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); }
 }
 @GetMapping("/{codigo}") public ResponseEntity<DisciplinaResponse> buscar(@PathVariable String codigo) {
  return repository.buscarPorCodigo(codigo).map(d -> ResponseEntity.ok(DisciplinaResponse.from(d))).orElseGet(() -> ResponseEntity.notFound().build());
 }
 private boolean vazio(String s) { return s == null || s.isBlank(); }
}
