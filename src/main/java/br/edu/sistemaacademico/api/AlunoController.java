package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
 private final AlunoRepository repository;
 public AlunoController(AlunoRepository repository) { this.repository = repository; }

 @PostMapping public ResponseEntity<?> criar(@RequestBody(required=false) CriarAlunoRequest request) {
  if (request == null || vazio(request.registroAcademico()) || vazio(request.nome()) || vazio(request.email())) return ResponseEntity.badRequest().body("Informe RA, nome e e-mail.");
  try { Aluno aluno = new Aluno(request.registroAcademico(), request.nome(), request.email()); repository.salvar(aluno);
   return ResponseEntity.created(URI.create("/api/alunos/" + aluno.getRegistroAcademico())).body(AlunoPersistidoResponse.from(aluno));
  } catch (IllegalArgumentException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); }
 }
 @GetMapping("/{identificador}") public ResponseEntity<AlunoPersistidoResponse> buscar(@PathVariable String identificador) {
  return repository.buscarPorRa(identificador).map(a -> ResponseEntity.ok(AlunoPersistidoResponse.from(a))).orElseGet(() -> ResponseEntity.notFound().build());
 }
 private boolean vazio(String s) { return s == null || s.isBlank(); }
}
