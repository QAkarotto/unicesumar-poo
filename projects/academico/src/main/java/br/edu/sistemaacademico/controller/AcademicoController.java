package br.edu.sistemaacademico.controller;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AcademicoController {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    // Injeção de dependências via construtor pelo Spring
    public AcademicoController(AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository) {
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    // --- ALUNOS ---

    @PostMapping("/alunos")
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno) {
        Aluno salvo = alunoRepository.salvar(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo); // ou ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/alunos/{ra}")
    public ResponseEntity<Aluno> buscarAlunoPorRa(@PathVariable String ra) {
        return alunoRepository.buscarPorRa(ra)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- DISCIPLINAS ---

    @PostMapping("/disciplinas")
    public ResponseEntity<Disciplina> cadastrarDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina salva = disciplinaRepository.salvar(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping("/disciplinas/{codigo}")
    public ResponseEntity<Disciplina> buscarDisciplinaPorCodigo(@PathVariable String codigo) {
        return disciplinaRepository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}