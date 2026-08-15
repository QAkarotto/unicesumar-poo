package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {

        var sistema = new SistemaAcademico();

        var notaProva = 5.5;
        var notaProjeto = 7.0;
        var notaLista = 6.5;
        var totalFaltas = 15;

        var mediaFinal = sistema.calcularMedia(notaProva, notaProjeto, notaLista);
        System.out.println("Média: " + mediaFinal);

        var status = sistema.verificarStatus(mediaFinal, totalFaltas);
        System.out.println("Status: " + status);

        var orientacao = sistema.gerarOrientacao(status);
        System.out.println("Orientação: " + orientacao);
    }

    public double calcularMedia(double nota1, double nota2, double nota3) {
        var soma = nota1 + nota2 + nota3;
        var media = soma / 3;
        return media;
    }

    public String verificarStatus(double media, int faltas) {
        if (faltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (media >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

    public String gerarOrientacao(String status) {
        return switch (status) {
            case "APROVADO" ->
                    "Parabéns! Você dominou Classes e Objetos. Boas férias!";
            case "EXAME" ->
                    "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" ->
                    "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default ->
                    "Procure a coordenação do curso.";
        };
    }
}