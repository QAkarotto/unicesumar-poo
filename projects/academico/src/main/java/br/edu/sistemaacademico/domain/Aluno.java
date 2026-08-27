package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

    public class Aluno {
        private String ra;
        private String nome;
        private String email;
        private final List<Matricula> matriculas = new ArrayList<>();

        public Aluno(String ra, String nome, String email) {
            if (ra == null || ra.equals("")) {
                throw new IllegalArgumentException("O RA é obrigatório.");
            }
            if (nome == null || nome.equals("")) {
                throw new IllegalArgumentException("O nome é obrigatório.");
            }
            validarEmail(email);
            this.ra = ra;
            this.nome = nome;
            this.email = email;
        }
        private void validarEmail(String email) {
            if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                throw new IllegalArgumentException("E-mail inválido.");
            }
        }
        public void adicionarMatricula(Matricula matricula){
            if (matricula == null) {
                throw new IllegalArgumentException("A matrícula é obrigatória.");
            }
            this.matriculas.add(matricula);
        }
        public boolean jaFoiAprovadoEm(Disciplina disciplina){
            if (disciplina == null){
                return false;
            }
            for (Matricula m: this.matriculas){
                if (disciplina.equals(m.getOferta().getDisciplina()) && m.getResultado()==ResultadoAcademico.APROVADO){
                    return true;
                }
            }
            return false;
        }
        public String getRa() {
            return this.ra;
        }
        public String getNome() {
            return this.nome;
        }
        public String getEmail() {
            return this.email;
        }
        public void setEmail(String email) {
            validarEmail(email);
            this.email = email;
        }
        public List<Matricula> getMatriculas(){
            return new ArrayList<>(this.matriculas);
        }
        @Override
        public String toString() {
            return this.ra + " - " + this.nome + " - " + this.email;
        }
    }
