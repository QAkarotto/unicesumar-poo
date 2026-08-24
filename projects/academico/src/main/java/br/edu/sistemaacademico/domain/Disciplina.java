package br.edu.sistemaacademico.domain;

import java.util.Objects;

/**
 * Representa um componente curricular do sistema acadêmico.
 *
 * <p>Duas disciplinas são consideradas a mesma disciplina quando possuem o
 * mesmo código, independentemente da instância usada — isso é o que permite
 * comparar disciplinas ofertadas em turmas e períodos diferentes (por
 * exemplo, para impedir uma nova matrícula em uma disciplina já concluída
 * com aprovação, mesmo que a oferta seja outro objeto).</p>
 */
public class Disciplina {

    private final String codigo;
    private final String nome;
    private final int cargaHoraria;

    public Disciplina(String codigo, String nome, int cargaHoraria) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da disciplina é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório.");
        }
        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária da disciplina deve ser positiva.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disciplina outra)) {
            return false;
        }
        return codigo.equals(outra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                '}';
    }
}
