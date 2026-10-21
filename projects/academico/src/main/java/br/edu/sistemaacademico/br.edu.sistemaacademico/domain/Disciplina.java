import java.util.Objects;

public class Disciplina {

    private final String codigo;
    private String descricao;
    private int horas;

    public Disciplina(
            String codigo,
            String descricao,
            int horas) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Codigo invalido."
            );
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Descricao obrigatoria."
            );
        }

        if (horas <= 0) {
            throw new IllegalArgumentException(
                    "Carga horaria deve ser maior que zero."
            );
        }

        this.codigo = codigo.trim();
        this.descricao = descricao.trim();
        this.horas = horas;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getHoras() {
        return horas;
    }

    /*
     * O código identifica a disciplina.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Disciplina)) {
            return false;
        }

        Disciplina outra = (Disciplina) obj;

        return codigo.equals(outra.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {

        return codigo + " - "
                + descricao
                + " (" + horas + "h)";
    }
}