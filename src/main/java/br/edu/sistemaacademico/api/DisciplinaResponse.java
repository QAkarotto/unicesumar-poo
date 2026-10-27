package br.edu.sistemaacademico.api;
import br.edu.sistemaacademico.domain.Disciplina;
public record DisciplinaResponse(String codigo, String nome, int cargaHoraria) {
 public static DisciplinaResponse from(Disciplina d) { return new DisciplinaResponse(d.getCodigo(), d.getNome(), d.getCargaHoraria()); }
}
