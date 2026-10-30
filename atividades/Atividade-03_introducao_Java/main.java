public class main {

    public static void main(String[] args) {
        var notaProva = 7.5;
        var notaProjeto = 8.0;
        var notaLista = 6.0;
        var faltas = 10;

        var media = CalculoNotas.calcularMedia(notaProva, notaProjeto, notaLista);
        var status = CalculoNotas.verificarStatus(media, faltas);
        var orientacao = CalculoNotas.gerarOrientacao(status);

        System.out.println("Media: " + media);
        System.out.println("Status: " + status);
        System.out.println("Orientacao: " + orientacao);
    }
}
