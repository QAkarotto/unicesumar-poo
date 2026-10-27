package br.edu.sistemaacademico.api;
import br.edu.sistemaacademico.domain.Aluno;
import java.util.List;
public record AlunoPersistidoResponse(String registroAcademico, String nome, String email, List<String> matriculas) {
 public static AlunoPersistidoResponse from(Aluno a) { return new AlunoPersistidoResponse(a.getRegistroAcademico(), a.getNome(), a.getEmail(), a.getMatriculas().stream().map(m -> m.getCodigo()).toList()); }
}
