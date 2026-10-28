package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;

import java.util.List;
import java.util.Optional;

public interface DisciplinaRepository {

    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorCodigo(String codigo);

    List<Disciplina> listarTodas();
}
