
package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import java.util.Optional;

public interface DisciplinaRepository {

    void salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorCodigo(String codigo);
}
