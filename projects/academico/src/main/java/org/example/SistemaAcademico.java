package org.example;

public class SistemaAcademico {

    public static void main(String[] args) {
        SistemaAcademico sistema = new SistemaAcademico();

        var notaProva = 5.6;
        var notaProjeto = 8.0;
        var notaLista = 6.7;
        var faltas = 13;

        var mediaFinal = sistema.calcularMedia(notaProva, notaProjeto, notaLista);
        var status = sistema.verificarStatus(mediaFinal, faltas);
        var orientacao = sistema.gerarOrientacao(status);

        System.out.println("Média Final: " + mediaFinal);
        System.out.println("Status: " + status);
        System.out.println("Orientação: " + orientacao);
    }

    public double calcularMedia(double prova, double projeto, double lista) {
        var media = (prova + projeto + lista) / 3.0;
        return media;
    }

  
    public String verificarStatus(double mediaFinal, int totalFaltas) {
        if (totalFaltas > 20) { 
            return "REPROVADO_POR_FALTA"; 
        } else if (mediaFinal >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }


    public String gerarOrientacao(String status) {
        return switch (status) {
            case "APROVADO" -> "Parabéns! Boas férias!";
            case "EXAME" -> "Atenção: Estude para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default -> "Procure a coordenação do curso.";
        };
    }
}

