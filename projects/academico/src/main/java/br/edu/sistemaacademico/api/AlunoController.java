package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "br.edu.sistemaacademico.api")
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private static final Map<String, Aluno> ALUNOS = new LinkedHashMap<>();
    private static final Map<String, OfertaDisciplina> OFERTAS = new LinkedHashMap<>();

    static {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        ALUNOS.put(paola.getRegistroAcademico(), paola);
        ALUNOS.put(bruno.getRegistroAcademico(), bruno);

        var turma = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var ofertaPoo = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        turma.ofertarDisciplina(new Disciplina("BD", "Banco de Dados", 80));
        ofertaPoo.matricular("MAT-001", paola);

        turma.getOfertas().forEach(oferta -> OFERTAS.put(chaveOferta(turma.getCodigo(),
                oferta.getDisciplina().getCodigo()), oferta));
    }

    public static void main(String[] args) {
        SpringApplication.run(AlunoController.class, args);
    }

    @GetMapping("/{registroAcademico}")
    public ResponseEntity<AlunoResposta> consultar(@PathVariable String registroAcademico) {
        var aluno = ALUNOS.get(registroAcademico);
        return aluno == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(AlunoResposta.de(aluno));
    }

    static Aluno obterAluno(String registroAcademico) {
        return ALUNOS.get(registroAcademico);
    }

    static OfertaDisciplina obterOferta(String codigoTurma, String codigoDisciplina) {
        return OFERTAS.get(chaveOferta(codigoTurma, codigoDisciplina));
    }

    static Collection<OfertaDisciplina> obterOfertas() {
        return OFERTAS.values();
    }

    private static String chaveOferta(String codigoTurma, String codigoDisciplina) {
        return codigoTurma + ":" + codigoDisciplina;
    }

    public record AlunoResposta(String registroAcademico, String nome, String email, List<String> matriculas) {
        static AlunoResposta de(Aluno aluno) {
            return new AlunoResposta(aluno.getRegistroAcademico(), aluno.getNome(), aluno.getEmail(),
                    aluno.getMatriculas().stream().map(matricula -> matricula.getCodigo()).toList());
        }
    }
}
