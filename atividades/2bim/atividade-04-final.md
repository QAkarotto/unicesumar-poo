# Atividade Final - 2BIM: Aplicação Full Stack com Spring Boot e ReactJS

## Objetivo

Desenvolver, **em trio**, uma aplicação completa integrando frontend, API REST e banco de dados relacional, aplicando os conceitos de Programação Orientada a Objetos trabalhados ao longo da disciplina.

O **tema da aplicação é livre**, desde que permita representar um domínio com regras de negócio, relacionamentos entre objetos e persistência de dados.

## Tecnologias obrigatórias

A solução deve utilizar:

- **Java 26**;
- **Spring Boot**;
- **Spring Web**;
- **JPA / Spring Data JPA**;
- **ReactJS** no frontend;
- **banco de dados relacional diferente de SQLite**.

O banco de dados recomendado é o **MySQL**.

Também podem ser utilizados outros bancos relacionais, como PostgreSQL ou MariaDB, desde que o grupo consiga configurar, documentar e executar corretamente a solução.

## Estrutura geral

A aplicação deve possuir:

- frontend em **ReactJS**;
- backend em **Java com Spring Boot**;
- **API REST** para comunicação entre frontend e backend;
- banco de dados SQL **modelado e implementado pelo trio**;
- persistência utilizando **JPA / Spring Data JPA**;
- backend organizado em camadas com responsabilidades bem definidas.

Fluxo esperado:

```text
ReactJS
   ↓
API REST
   ↓
Controller
   ↓
Service
   ↓
Domain / Entity
   ↓
Repository
   ↓
JPA / Hibernate
   ↓
Banco de Dados SQL
```

## Exemplo de organização do backend

Utilizando o **Sistema Acadêmico** como referência:

```text
src/
└── main/
    └── java/
        └── br/
            └── edu/
                └── sistemaacademico/
                    ├── SistemaAcademicoApplication.java
                    │
                    ├── controller/
                    │   ├── AlunoController.java
                    │   ├── DisciplinaController.java
                    │   └── MatriculaController.java
                    │
                    ├── service/
                    │   ├── AlunoService.java
                    │   ├── DisciplinaService.java
                    │   └── MatriculaService.java
                    │
                    ├── repository/
                    │   ├── AlunoRepository.java
                    │   ├── DisciplinaRepository.java
                    │   └── MatriculaRepository.java
                    │
                    ├── domain/
                    │   ├── Aluno.java
                    │   ├── Disciplina.java
                    │   ├── Matricula.java
                    │   └── OfertaDisciplina.java
                    │
                    ├── dto/
                    │   ├── CriarAlunoRequest.java
                    │   ├── CriarMatriculaRequest.java
                    │   ├── AlunoResponse.java
                    │   └── MatriculaResponse.java
                    │
                    └── exception/
                        ├── RecursoNaoEncontradoException.java
                        └── GlobalExceptionHandler.java
```

Essa estrutura é apenas uma referência. Outras organizações são permitidas, desde que as responsabilidades estejam claramente separadas.

## Responsabilidade das camadas

### Controller

Responsável pela comunicação HTTP.

Exemplo:

```java
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping("/{ra}")
    public AlunoResponse buscar(@PathVariable String ra) {
        return service.buscarPorRa(ra);
    }
}
```

O Controller deve concentrar responsabilidades relacionadas a:

- rotas;
- parâmetros;
- corpo das requisições;
- respostas;
- status HTTP.

As regras de negócio não devem ficar concentradas nessa camada.

### Service

Responsável por coordenar os **casos de uso da aplicação**.

Exemplo:

```java
@Service
public class MatriculaService {

    private final AlunoRepository alunoRepository;
    private final OfertaDisciplinaRepository ofertaRepository;
    private final MatriculaRepository matriculaRepository;

    public MatriculaService(
            AlunoRepository alunoRepository,
            OfertaDisciplinaRepository ofertaRepository,
            MatriculaRepository matriculaRepository) {

        this.alunoRepository = alunoRepository;
        this.ofertaRepository = ofertaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public Matricula matricular(String ra, Long ofertaId) {

        var aluno = alunoRepository.findById(ra)
                .orElseThrow();

        var oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow();

        var matricula = oferta.matricular(aluno);

        return matriculaRepository.save(matricula);
    }
}
```

O Service coordena as operações, mas não deve substituir comportamentos que pertencem aos objetos do domínio.

### Domain / Entity

Representa os conceitos principais e suas regras.

Exemplo:

```java
@Entity
public class Matricula {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private SituacaoMatricula situacao;

    @Enumerated(EnumType.STRING)
    private ResultadoAcademico resultado;

    public void concluir(ResultadoAcademico resultado) {

        if (situacao != SituacaoMatricula.ATIVA) {
            throw new IllegalStateException(
                    "A matrícula não está ativa."
            );
        }

        this.resultado = resultado;
        this.situacao = SituacaoMatricula.CONCLUIDA;
    }
}
```

É nessa camada que devem aparecer, quando aplicável:

- encapsulamento;
- regras do domínio;
- invariantes;
- mudanças de estado;
- composição;
- colaboração entre objetos;
- relacionamentos.

### Repository

Responsável pelo acesso aos dados.

Com Spring Data JPA:

```java
public interface AlunoRepository
        extends JpaRepository<Aluno, String> {

    Optional<Aluno> findByEmail(String email);
}
```

Diferentemente da atividade anterior, na qual o acesso aos dados foi implementado manualmente utilizando **JDBC, SQL, `PreparedStatement` e `ResultSet`**, neste projeto deverá ser utilizado **JPA com Spring Data JPA**.

O trio deve compreender quais responsabilidades o JPA passou a abstrair em relação à implementação realizada anteriormente com JDBC.

## DTOs

DTOs ou `record` podem ser utilizados para representar os dados que entram ou saem da API.

Exemplo:

```java
public record CriarMatriculaRequest(
    String raAluno,
    Long idOferta
) {}
```

Resposta:

```java
public record MatriculaResponse(
    Long id,
    String aluno,
    String disciplina,
    String situacao
) {}
```

O formato da API não precisa ser exatamente igual à estrutura interna das entidades.

## Modelagem e implementação do banco de dados

O trio deverá **modelar e implementar o banco de dados relacional da aplicação**.

Não será fornecida uma estrutura pronta.

A modelagem deverá considerar:

- entidades necessárias ao domínio;
- tabelas;
- atributos e tipos;
- chaves primárias;
- chaves estrangeiras;
- relacionamentos;
- obrigatoriedade dos campos;
- restrições relevantes;
- cardinalidades entre as entidades.

Utilizando o Sistema Acadêmico como exemplo:

```text
ALUNO
  ↓
MATRICULA
  ↓
OFERTA_DISCIPLINA
  ↓
DISCIPLINA
```

Uma possível estrutura relacional:

```text
ALUNO
- ra PK
- nome
- email

DISCIPLINA
- id PK
- codigo
- nome
- carga_horaria

OFERTA_DISCIPLINA
- id PK
- disciplina_id FK
- turma_id FK

MATRICULA
- id PK
- aluno_ra FK
- oferta_id FK
- situacao
- resultado
```

Esse exemplo não deve ser copiado obrigatoriamente. Cada trio deve modelar o banco de acordo com o domínio escolhido.

## JPA e relacionamentos

A modelagem Java deve representar adequadamente os relacionamentos necessários.

Podem ser utilizados, quando fizerem sentido:

```java
@OneToOne
@OneToMany
@ManyToOne
@ManyToMany
```

Exemplo:

```java
@Entity
public class Matricula {

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private OfertaDisciplina oferta;
}
```

O trio deve ser capaz de explicar a relação entre:

```text
relacionamento entre objetos
        ↓
mapeamento JPA
        ↓
chave estrangeira no banco
```

## Script SQL

Mesmo utilizando JPA, o projeto deverá conter um **script SQL representando a estrutura do banco**.

Exemplo:

```text
database/
└── schema.sql
```

O script deve conter pelo menos:

- `CREATE TABLE`;
- chaves primárias;
- chaves estrangeiras;
- restrições necessárias.

Não utilize apenas a geração automática do Hibernate como substituição da modelagem.

Configurações como:

```properties
spring.jpa.hibernate.ddl-auto=create
```

podem ser utilizadas durante o desenvolvimento, mas **não eliminam a necessidade do `schema.sql`**.

## Banco de dados

O projeto deve utilizar um **banco de dados relacional diferente de SQLite**.

A opção preferencial é:

- **MySQL**.

Também são aceitos:

- PostgreSQL;
- MariaDB;
- outro SGBD relacional previamente justificado.

O README deve informar:

- qual banco foi utilizado;
- como criar o banco;
- como configurar usuário e senha;
- como executar o `schema.sql`;
- como configurar a conexão do Spring Boot.

Não envie credenciais pessoais ou senhas reais no repositório.

## Frontend com ReactJS

O frontend deverá ser desenvolvido obrigatoriamente com **ReactJS**.

Ele deve consumir a API REST e permitir executar os principais fluxos da aplicação.

Não é necessário desenvolver uma interface visual complexa. O foco está em:

- integração com a API;
- envio e recebimento de JSON;
- formulários;
- listagens;
- navegação entre funcionalidades;
- tratamento básico de erros;
- atualização da interface após as operações.

Utilizando o Sistema Acadêmico como exemplo:

```text
Tela de alunos
    ↓
Cadastrar aluno
    ↓
POST /api/alunos

Tela de matrícula
    ↓
Matricular aluno
    ↓
POST /api/matriculas

Histórico acadêmico
    ↓
Consultar dados
    ↓
GET /api/alunos/{ra}
```

## Requisitos mínimos

A aplicação deve possuir pelo menos:

- **3 entidades ou conceitos principais relacionados entre si**;
- modelagem relacional correspondente;
- operações de cadastro e consulta;
- pelo menos uma operação de atualização ou exclusão;
- pelo menos **2 regras de negócio** além de CRUD;
- persistência utilizando **JPA / Spring Data JPA**;
- banco relacional diferente de SQLite;
- frontend em **ReactJS**;
- tratamento de dados inválidos;
- tratamento de operações não permitidas;
- respostas HTTP coerentes;
- integração completa entre frontend, backend e banco;
- testes automatizados para regras relevantes.

## API REST

Utilize adequadamente os métodos HTTP necessários:

- `GET`;
- `POST`;
- `PUT` ou `PATCH`;
- `DELETE`, quando aplicável.

Exemplo no Sistema Acadêmico:

```text
POST   /api/alunos
GET    /api/alunos/{ra}

POST   /api/disciplinas
GET    /api/disciplinas/{id}

POST   /api/matriculas
GET    /api/matriculas/{id}
PATCH  /api/matriculas/{id}/conclusao
```

## Testes

Implemente testes automatizados para algumas das regras de negócio mais importantes.

Exemplo:

```java
@Test
void naoDevePermitirNovaMatriculaAposAprovacao() {
    // Arrange

    // Act

    // Assert
}
```

Evite priorizar testes de getters, setters ou outros comportamentos triviais.

## Tema

O tema é livre.

Algumas possibilidades:

- gerenciamento de eventos;
- biblioteca;
- reservas;
- controle de estoque;
- pedidos;
- campeonatos;
- controle financeiro;
- agenda de atendimentos;
- locação;
- gerenciamento de tarefas.

Evite sistemas excessivamente simples que não permitam demonstrar relacionamentos e regras de negócio.

## Organização do trio

A atividade deverá ser realizada em **trios**.

Todos os integrantes devem participar do desenvolvimento e compreender a solução completa.

A divisão de tarefas pode considerar frontend, backend e banco de dados, mas cada integrante deve ser capaz de explicar a integração entre essas partes e as principais decisões adotadas pelo grupo.

## Entrega

A entrega deve conter:

- código completo do frontend em ReactJS;
- código completo do backend;
- modelagem do banco de dados;
- `schema.sql`;
- configuração de persistência;
- testes automatizados;
- README com instruções completas para execução.

O README deve informar:

- integrantes do trio;
- tema e objetivo do sistema;
- tecnologias utilizadas;
- banco escolhido;
- como criar e configurar o banco;
- como executar o backend;
- como executar o frontend;
- principais funcionalidades.

O projeto deve ser executável seguindo as instruções presentes no repositório.

## Apresentação

O trio deverá demonstrar o funcionamento completo:

```text
ReactJS
    ↓
Controller
    ↓
Service
    ↓
Domain / Entity
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
Banco SQL
```

Durante a apresentação, **todos os integrantes poderão ser questionados** sobre:

- organização das camadas;
- responsabilidades das classes;
- regras de negócio;
- modelagem orientada a objetos;
- modelagem relacional;
- relacionamentos e chaves estrangeiras;
- mapeamentos JPA;
- diferenças entre JDBC e JPA;
- persistência;
- endpoints;
- integração com ReactJS;
- tratamento de erros;
- testes;
- decisões de design.

A apresentação deve demonstrar compreensão da solução desenvolvida, e não apenas o funcionamento visual da aplicação.
