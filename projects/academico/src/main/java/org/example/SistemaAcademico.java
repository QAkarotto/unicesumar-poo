package org.example;

public class SistemaAcademico {


    public static double calcularMedia(double notaProva, double notaProjeto, double notaLista) {
        var soma  = notaProva + notaProjeto + notaLista;
        var media = soma / 3;
        return media;
    }


    public static String verificarStatus(double media, int totalFaltas) {
        if (totalFaltas > 20) {
            return "REPROVADO_POR_FALTA";
        } else if (media >= 6.0) {
            return "APROVADO";
        } else {
            return "EXAME";
        }
    }

    public static String gerarOrientacao(String status) {
        return switch (status) {
            case "APROVADO"            -> "Parabéns! Você dominou Classes e Objetos. Boas férias!";
            case "EXAME"               -> "Atenção: Estude os conceitos de Herança e Polimorfismo para a prova substitutiva.";
            case "REPROVADO_POR_FALTA" -> "Reprovação automática. Frequência abaixo do mínimo exigido.";
            default                    -> "Procure a coordenação do curso.";
        };
    }

    public static void main(String[] args) {
        // Teste rápido
        double media = calcularMedia(7.0, 5.5, 8.0);
        System.out.println("Média: " + media);

        String status = verificarStatus(media, 10);
        System.out.println("Status: " + status);

        String orientacao = gerarOrientacao(status);
        System.out.println("Orientação: " + orientacao);

        // Caso com reprovação por falta
        String s2 = verificarStatus(8.0, 25);
        System.out.println(gerarOrientacao(s2));
    }
}
