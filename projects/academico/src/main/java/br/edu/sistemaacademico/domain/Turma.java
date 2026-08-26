package br.edu.sistemaacademico.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * A turma deixou de ser "uma disciplina em um período" e passou a ser
 * um GRUPO ACADÊMICO que oferta várias disciplinas em um período letivo.
 */
public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;

    /*
     * Mapa com a Disciplina como chave: como Disciplina define equals/hashCode
     * pelo código, o próprio Map já garante que não existe oferta duplicada.
     * LinkedHashMap preserva a ordem em que as disciplinas foram ofertadas.
     */
    private final Map<Disciplina, OfertaDisciplina> ofertas = new LinkedHashMap<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da turma é obrigatório e não pode ser vazio.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("Período letivo da turma é obrigatório.");
        }
        this.codigo = codigo.trim();
        this.periodoLetivo = periodoLetivo;
    }

    // ---------- Métodos de acesso ----------

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    /*
     * Cópia protegida: ninguém adiciona oferta por fora da regra da turma.
     */
    public List<OfertaDisciplina> getOfertas() {
        return List.copyOf(ofertas.values());
    }

    public boolean ofertaDisciplina(Disciplina disciplina) {
        return disciplina != null && ofertas.containsKey(disciplina);
    }

    // ---------- Regra de negócio ----------

    /*
     * A turma é a única responsável por criar suas ofertas.
     * É aqui que a duplicidade de disciplina é barrada.
     */
    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina é obrigatória para realizar a oferta.");
        }
        if (ofertas.containsKey(disciplina)) {
            throw new IllegalStateException(
                    "A disciplina " + disciplina.getCodigo()
                            + " já está ofertada na turma " + codigo + "."
            );
        }
        var oferta = new OfertaDisciplina(this, disciplina);
        ofertas.put(disciplina, oferta);
        return oferta;
    }

    @Override
    public String toString() {
        // Não imprime as ofertas de propósito: evita recursão com OfertaDisciplina.
        return "Turma [" + codigo + "] " + periodoLetivo;
    }
}