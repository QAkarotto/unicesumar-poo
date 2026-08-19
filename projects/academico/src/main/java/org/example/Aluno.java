package org.example;

public class Aluno {
    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {

        if(identificadorAcademico == null || identificadorAcademico.isEmpty()) {
            throw new IllegalArgumentException("Identificador acadêmico não pode ser nulo ou vazio.");
        }

        validarNome(nome);
        validarEmail(email);
        
        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }
    
    private void validarNome(String nome) {
        if(nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
    }
    
    private void validarEmail(String email) {
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio.");
        }
    }

    public void alterarDados(String nome, String email) {
        validarNome(nome);
        validarEmail(email);
        this.nome = nome;
        this.email = email;

        
    }
    public String getIdentificadorAcademico() { return identificadorAcademico; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
    return "Aluno{" +"identificadorAcademico='" + identificadorAcademico + '\'' +", nome='" + nome + '\'' +", email='" + email + '\'' +'}';
}
}