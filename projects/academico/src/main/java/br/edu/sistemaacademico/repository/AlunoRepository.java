
package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import java.util.Optional;

public interface AlunoRepository {

    void salvar(Aluno aluno);

    Optional<Aluno> buscarPorRegistroAcademico(String registroAcademico);
}
