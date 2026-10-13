public class Main {

    public static void main(String[] args) {

        AvaliacaoAluno avaliacao = new AvaliacaoAluno();

        double prova = 8.0;
        double projeto = 7.0;
        double lista = 9.0;

        int faltas = 10;

        double media = avaliacao.calcularMedia(prova, projeto, lista);

        String status = avaliacao.verificarStatus(media, faltas);

        String orientacao = avaliacao.gerarOrientacao(status);

        System.out.println("===== RESULTADO DO ALUNO =====");
        System.out.println("Prova: " + prova);
        System.out.println("Projeto: " + projeto);
        System.out.println("Lista: " + lista);
        System.out.println("Faltas: " + faltas);
        System.out.println("Média: " + media);
        System.out.println("Status: " + status);
        System.out.println("Orientação: " + orientacao);
    }
}