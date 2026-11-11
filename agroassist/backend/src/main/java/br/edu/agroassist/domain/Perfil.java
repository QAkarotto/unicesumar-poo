package br.edu.agroassist.domain;

public enum Perfil {
    PRODUTOR("produtor"),
    TECNICO("tecnico"),
    ADMIN("admin");
    private final String valor;

    Perfil(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
