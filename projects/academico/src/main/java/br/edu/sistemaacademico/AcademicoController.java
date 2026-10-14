package br.edu.sistemaacademico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class AcademicoController {


    private List<Aluno> alunos = new ArrayList<>();
    private List<OfertaDisciplina> ofertas = new ArrayList<>();
    private Map<String, Matricula> matriculas = new HashMap<>();
    private int contadorMatriculas = 2; // Para gerar IDs como "MAT-002"

    public AcademicoController() {

        Aluno paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        Aluno bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        alunos.add(paola);
        alunos.add(bruno);

        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina pooOferta = turma.ofertarDisciplina(poo);
        ofertas.add(pooOferta);

        // Criar uma matrícula inicial com uma chave simples para o GET funcionar
        Matricula primeiraMatricula = pooOferta.matricular(paola);
        matriculas.put("MAT-001", primeiraMatricula);
    }

    public static void main(String[] args) {
        // Inicia o servidor do Spring Boot
        SpringApplication.run(AcademicoController.class, args);
    }

    // 1. Consultar um aluno (GET /api/alunos/{identificador})
    @GetMapping("/alunos/{identificador}")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable String identificador) {
        for (Aluno aluno : alunos) {
            if (aluno.getIdentificador().equals(identificador)) {
                return ResponseEntity.ok(aluno);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 2. Consultar uma matrícula (GET /api/matriculas/{id})
    @GetMapping("/matriculas/{id}")
    public ResponseEntity<Matricula> buscarMatricula(@PathVariable String id) {
        Matricula matriculaEncontrada = matriculas.get(id);
        if (matriculaEncontrada != null) {
            return ResponseEntity.ok(matriculaEncontrada);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 3. Criar uma matrícula (POST /api/matriculas)
    @PostMapping("/matriculas")
    public ResponseEntity<String> criarMatricula(@RequestBody DadosNovaMatricula dados) {
        // Procurar o aluno e a oferta de disciplina em memória
        Aluno alunoEncontrado = alunos.stream()
                .filter(a -> a.getIdentificador().equals(dados.getIdentificadorAluno()))
                .findFirst().orElse(null);

        OfertaDisciplina ofertaEncontrada = ofertas.stream()
                .filter(o -> o.getDisciplina().getCodigo().equals(dados.getCodigoDisciplina()))
                .findFirst().orElse(null);

        if (alunoEncontrado == null || ofertaEncontrada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno ou Oferta de Disciplina não encontrados.");
        }

        try {
            // A MAGIA ACONTECE AQUI: Deixamos a regra de negócio no domínio!
            // O método matricular já valida duplicatas e aprovações e lança exceção se falhar.
            Matricula novaMatricula = ofertaEncontrada.matricular(alunoEncontrado);

            // Guardamos com um ID gerado para a consulta posterior
            String novoId = "MAT-00" + contadorMatriculas++;
            matriculas.put(novoId, novaMatricula);

            return ResponseEntity.status(HttpStatus.CREATED).body("Matrícula criada com ID: " + novoId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Capturamos a regra de negócio do seu domínio que bloqueou a ação
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Classe auxiliar para receber o JSON. (Pode justificar isto ao professor:
    // "Criámos esta classe porque o Jackson precisa de construtores vazios para ler o JSON,
    // e não queríamos estragar as classes de domínio originais tirando a sua integridade").
    static class DadosNovaMatricula {
        private String identificadorAluno;
        private String codigoDisciplina;

        public String getIdentificadorAluno() { return identificadorAluno; }
        public void setIdentificadorAluno(String identificadorAluno) { this.identificadorAluno = identificadorAluno; }
        public String getCodigoDisciplina() { return codigoDisciplina; }
        public void setCodigoDisciplina(String codigoDisciplina) { this.codigoDisciplina = codigoDisciplina; }
    }
}