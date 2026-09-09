import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaAcademico {

    static Scanner scanner = new Scanner(System.in);

    static List<Aluno> alunos = new ArrayList<>();
    static List<Disciplina> disciplinas = new ArrayList<>();
    static List<Turma> turmas = new ArrayList<>();
    static List<Matricula> matriculas = new ArrayList<>();

    public static void main(String[] args) {


        Disciplina disciplinaPOO =
                new Disciplina(
                        "POO001",
                        "Programação Orientada a Objetos",
                        80
                );

        Disciplina disciplinaBD =
                new Disciplina(
                        "BD001",
                        "Banco de Dados",
                        60
                );

        Disciplina disciplinaREQ =
                new Disciplina(
                        "REQ001",
                        "Engenharia de Requisitos",
                        50
                );

        Disciplina disciplinaSO =
                new Disciplina(
                        "SO001",
                        "Sistemas Operacionais",
                        40
                );

        Disciplina disciplinaPS =
                new Disciplina(
                        "PS001",
                        "Projeto de Software",
                        60
                );

        disciplinas.add(disciplinaPOO);
        disciplinas.add(disciplinaBD);
        disciplinas.add(disciplinaREQ);
        disciplinas.add(disciplinaSO);
        disciplinas.add(disciplinaPS);

        PeriodoLetivo periodo2026 =
                new PeriodoLetivo(
                        2026,
                        Semestre.SEGUNDO
                );

        Turma turmaESOFT =
                new Turma(
                        "ESOFT4S-NA",
                        disciplinaPOO,
                        periodo2026
                );

        turmaESOFT.adicionarDisciplina(disciplinaBD);
        turmaESOFT.adicionarDisciplina(disciplinaSO);
        turmaESOFT.adicionarDisciplina(disciplinaREQ);

        turmas.add(turmaESOFT);

        Turma turmaADSIS =
                new Turma(
                        "ADSIS4S",
                        disciplinaPOO,
                        periodo2026
                );

        turmaADSIS.adicionarDisciplina(disciplinaBD);
        turmaADSIS.adicionarDisciplina(disciplinaSO);
        turmaADSIS.adicionarDisciplina(disciplinaPS);

        turmas.add(turmaADSIS);

        menuPrincipal();
    }

    public static void menuPrincipal() {

        int opcao;

        do {
            System.out.println("\n=================================");
            System.out.println("       SISTEMA ACADÊMICO");
            System.out.println("=================================");
            System.out.println("1 - Cadastrar Aluno");
            System.out.println("2 - Entrar como Aluno");
            System.out.println("3 - Ver Disciplinas");
            System.out.println("4 - Ver Turmas");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    cadastrarAluno();
                    break;

                case 2:
                    loginAluno();
                    break;

                case 3:
                    mostrarDisciplinas();
                    break;

                case 4:
                    mostrarTurmas();
                    break;

                case 0:
                    System.out.println(
                            "\nSistema encerrado."
                    );
                    break;

                default:
                    System.out.println(
                            "\nOpção inválida!"
                    );
            }

        } while (opcao != 0);
    }

    public static void cadastrarAluno() {

        System.out.println(
                "\n===== CADASTRO DO ALUNO ====="
        );

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        String id =
                "ALUNO" + (alunos.size() + 1);

        Aluno aluno =
                new Aluno(
                        id,
                        nome,
                        email
                );

        alunos.add(aluno);

        System.out.println(
                "\nAluno cadastrado com sucesso!"
        );

        System.out.println(
                "Seu ID acadêmico: " + id
        );
    }

    public static void loginAluno() {

        System.out.println(
                "\n===== LOGIN DO ALUNO ====="
        );

        if (alunos.isEmpty()) {

            System.out.println(
                    "Nenhum aluno cadastrado."
            );

            System.out.println(
                    "Faça seu cadastro primeiro."
            );

            return;
        }

        System.out.print(
                "Digite seu ID acadêmico: "
        );

        String id = scanner.nextLine();

        Aluno alunoEncontrado = null;

        for (Aluno aluno : alunos) {

            if (aluno
                    .getIdentificadorAcademico()
                    .equalsIgnoreCase(id)) {

                alunoEncontrado = aluno;
                break;
            }
        }

        if (alunoEncontrado == null) {

            System.out.println(
                    "\nAluno não encontrado."
            );

            return;
        }

        System.out.println(
                "\nLogin realizado com sucesso!"
        );

        System.out.println(
                "Bem-vindo, "
                        + alunoEncontrado.getNome()
                        + "!"
        );

        menuAluno(alunoEncontrado);
    }

    public static void menuAluno(Aluno aluno) {

        int opcao;

        do {

            System.out.println(
                    "\n===== MENU DO ALUNO ====="
            );

            System.out.println(
                    "Aluno: " + aluno.getNome()
            );

            System.out.println(
                    "--------------------------------"
            );

            System.out.println(
                    "1 - Ver meus dados"
            );

            System.out.println(
                    "2 - Ver minhas matrículas"
            );

            System.out.println(
                    "3 - Ver disciplinas"
            );

            System.out.println(
                    "4 - Ver semestre"
            );

            System.out.println(
                    "5 - Fazer matrícula"
            );

            System.out.println(
                    "0 - Sair"
            );

            System.out.print(
                    "Escolha: "
            );

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    aluno.mostrarDados();
                    break;

                case 2:
                    aluno.mostrarMatriculas();
                    break;

                case 3:
                    mostrarDisciplinas();
                    break;

                case 4:
                    mostrarSemestres();
                    break;

                case 5:
                    fazerMatricula(aluno);
                    break;

                case 0:
                    System.out.println(
                            "Saindo da conta..."
                    );
                    break;

                default:
                    System.out.println(
                            "Opção inválida!"
                    );
            }

        } while (opcao != 0);
    }

    public static void fazerMatricula(
            Aluno aluno) {

        System.out.println(
                "\n===== FAZER MATRÍCULA ====="
        );

        if (turmas.isEmpty()) {

            System.out.println(
                    "Nenhuma turma disponível."
            );

            return;
        }

        mostrarTurmas();

        System.out.print(
                "\nDigite o código da turma: "
        );

        String codigo = scanner.nextLine();

        Turma turmaEscolhida = null;

        for (Turma turma : turmas) {

            if (turma
                    .getCodigo()
                    .equalsIgnoreCase(codigo)) {

                turmaEscolhida = turma;
                break;
            }
        }

        if (turmaEscolhida == null) {

            System.out.println(
                    "Turma não encontrada."
            );

            return;
        }

        for (Matricula matricula :
                aluno.getMatriculas()) {

            if (matricula
                    .getTurma()
                    .getCodigo()
                    .equalsIgnoreCase(codigo)) {

                System.out.println(
                        "Você já está matriculado nessa turma."
                );

                return;
            }
        }

        String codigoMatricula =
                "MAT" + (matriculas.size() + 1);

        Matricula matricula =
                new Matricula(
                        codigoMatricula,
                        aluno,
                        turmaEscolhida
                );

        matriculas.add(matricula);

        aluno.adicionarMatricula(matricula);

        System.out.println(
                "\nMatrícula realizada com sucesso!"
        );

        System.out.println(
                "Código da matrícula: "
                        + codigoMatricula
        );
    }

    public static void mostrarDisciplinas() {

        System.out.println(
                "\n===== DISCIPLINAS ====="
        );

        if (disciplinas.isEmpty()) {

            System.out.println(
                    "Nenhuma disciplina cadastrada."
            );

            return;
        }

        for (Disciplina disciplina :
                disciplinas) {

            disciplina.mostrarDados();
        }
    }

    public static void mostrarSemestres() {

        System.out.println(
                "\n===== SEMESTRES ====="
        );

        for (Semestre semestre :
                Semestre.values()) {

            System.out.println(
                    "- " + semestre
            );
        }

        System.out.println(
                "\n===== PERÍODOS DAS TURMAS ====="
        );

        for (Turma turma : turmas) {

            PeriodoLetivo periodo =
                    turma.getPeriodoLetivo();

            System.out.println(
                    turma.getCodigo()
                            + " -> "
                            + periodo.getAno()
                            + " - "
                            + periodo.getSemestre()
            );
        }
    }

    public static void mostrarTurmas() {

        System.out.println(
                "\n===== TURMAS DISPONÍVEIS ====="
        );

        if (turmas.isEmpty()) {

            System.out.println(
                    "Nenhuma turma cadastrada."
            );

            return;
        }

        for (Turma turma : turmas) {

            turma.mostrarDados();
        }
    }
}