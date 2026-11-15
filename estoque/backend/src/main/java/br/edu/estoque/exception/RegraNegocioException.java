package br.edu.estoque.exception;

public class RegraNegocioException extends RuntimeException {
    private final String campo;

    public RegraNegocioException(String msg) { this(null, msg); }

    /** campo indica qual campo do formulário causou o erro (pode ser null). */
    public RegraNegocioException(String campo, String msg) { super(msg); this.campo = campo; }

    public String getCampo() { return campo; }
}
