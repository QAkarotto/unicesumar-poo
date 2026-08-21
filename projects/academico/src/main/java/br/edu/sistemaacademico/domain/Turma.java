package br.edu.sistemaacademico.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// A turma é o grupo de um período letivo e é ela quem sabe quais disciplinas
// são ofertadas. Código e período continuam final: trocar um dos dois seria
// outra turma.
public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodoLetivo;

    // O mapa usa o código da disciplina como chave, então a própria chave
    // impede a oferta repetida. LinkedHashMap para manter a ordem da oferta.
    private final Map<String, OfertaDisciplina> ofertas = new LinkedHashMap<>();

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        this.codigo = validarCodigo(codigo);
        this.periodoLetivo = validarPeriodoLetivo(periodoLetivo);
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    // Cópia: quem consulta as ofertas não consegue alterar a turma.
    public List<OfertaDisciplina> getOfertas() {
        return List.copyOf(ofertas.values());
    }

    public boolean possuiOferta(Disciplina disciplina) {
        return disciplina != null && ofertas.containsKey(disciplina.getCodigo());
    }

    // Quem cria a oferta é a turma, porque é ela que sabe o que já foi ofertado.
    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A oferta precisa de uma disciplina.");
        }

        if (possuiOferta(disciplina)) {
            throw new IllegalStateException("A disciplina " + disciplina.getCodigo()
                    + " já é ofertada pela turma " + codigo + ".");
        }

        var oferta = new OfertaDisciplina(this, disciplina);
        ofertas.put(disciplina.getCodigo(), oferta);
        return oferta;
    }

    private static String validarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        return codigo.strip();
    }

    private static PeriodoLetivo validarPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("A turma precisa de um período letivo.");
        }
        return periodoLetivo;
    }

    @Override
    public String toString() {
        return codigo + " | " + periodoLetivo
                + " | " + ofertas.size() + " disciplina(s) ofertada(s)";
    }
}
