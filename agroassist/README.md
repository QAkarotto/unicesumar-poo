# AgroAssist

Aplicação acadêmica de assistência técnica rural. O produtor cadastra propriedades e solicita ajuda para a lavoura; a coordenação atribui um técnico habilitado; o técnico registra o atendimento; o produtor acompanha e avalia.

Este monorepo reaproveita a interface do AgroAssist original e substitui a API simulada e o armazenamento no navegador por **Java 26 + Spring Boot + JPA + MySQL**. O frontend continua em **React com TypeScript**.

## Integrantes do trio

- Alexandre Gaia da Silva
- Daniel Martins Ciriaco Nicoli

Preencham o nome do terceiro integrante antes da entrega. Os nomes dos usuários fictícios da demonstração não representam automaticamente os integrantes do trabalho.

## Tecnologias e organização

| Parte        | Tecnologias                                                 |
| ------------ | ----------------------------------------------------------- |
| Backend      | Java 26, Spring Boot 4.0.9, Spring Web MVC, Bean Validation |
| Persistência | Spring Data JPA / Hibernate e MySQL 8.4                     |
| Frontend     | React 18, TypeScript 5.6, Vite 5 e React Router 6           |
| Testes       | JUnit, com testes das regras do domínio                     |
| Build        | Maven no backend e npm no frontend                          |

```text
agroassist/
  backend/
    pom.xml
    src/main/java/br/edu/agroassist/
      controller/       Rotas, JSON e status HTTP
      service/          Casos de uso e transações
      domain/           Entidades e regras de negócio
      repository/       Interfaces Spring Data JPA
      dto/              Dados de entrada e saída da API
      exception/        Erros e tratamento HTTP
      config/           Dados iniciais e configuração web
    src/main/resources/application.properties
    src/test/java/      Testes automatizados das regras
  frontend/
    src/                Telas e componentes originais em React
    src/services/       Chamadas HTTP e estado da interface
  database/
    schema.sql          Estrutura SQL escrita explicitamente
    seed.sql            Dados fictícios da demonstração
    modelo.md           Entidades, cardinalidades e restrições
```

O MySQL roda localmente no computador. O backend pode ser iniciado pelo IntelliJ, e o frontend é iniciado em um terminal separado.

## 1. Pré-requisitos

- **JDK 26**. O enunciado pede esta versão, e o projeto está configurado para ela.
- **IntelliJ IDEA** para abrir e executar o backend.
- **Node.js 22 ou 24 LTS**, com npm, para o frontend.
- **MySQL Server 8.4** instalado e configurado no Windows.
- **MySQL Workbench**, se preferir criar o banco pela interface gráfica.
- **Maven 3.9 ou mais recente**, se for executar os comandos Maven no terminal. No IntelliJ, também é possível usar o Maven integrado à IDE.

Confira o Java e o Node no terminal:

```powershell
java -version
node -v
npm -v
```

Para usar Maven pelo terminal, confira também `mvn -v`. A saída deve informar Java 26. Se aparecer outra versão, ajuste `JAVA_HOME`. No IntelliJ, configure o **Project SDK** e o JDK usado pelo Maven como **26**.

## 2. Preparar o MySQL local

### Instalar e iniciar o servidor

Se o MySQL Server 8.4 já está instalado e funcionando, prossiga para a criação do banco.

1. Instale o **MySQL Server 8.4** pelo instalador MSI para Windows e abra o **MySQL Configurator** ao terminar. O passo de configuração é necessário para iniciar o servidor.
2. No Configurator, mantenha a porta **3306**, defina uma senha para o usuário administrador `root` e configure o MySQL como **serviço do Windows**, com início automático. Essa senha de `root` é a que você escolheu na instalação.
3. Para conferir se está rodando, pressione **Win + R**, digite `services.msc` e abra **Serviços**. Localize o serviço com o nome definido no Configurator, por exemplo `MySQL84`. Se estiver parado, clique em **Iniciar**.

As etapas de instalação e configuração estão na [documentação oficial para Windows](https://dev.mysql.com/doc/refman/8.4/en/windows-installation.html). O serviço mantém o servidor disponível quando o Windows inicia; consulte também [como iniciar o serviço](https://dev.mysql.com/doc/refman/8.4/en/windows-start-service.html).

### Criar o banco pelo MySQL Workbench

1. Abra o **MySQL Workbench** e conecte-se ao servidor local com host `localhost`, porta `3306` e usuário `root`. Informe a senha definida na instalação.
2. Abra **File → Open SQL Script** e selecione `database/schema.sql` dentro da pasta extraída do projeto.
3. Execute **todo o script** pelo botão de execução do editor SQL. Ele cria o banco `agroassist`, as tabelas, as chaves e as restrições.
4. Em uma nova aba SQL, execute os comandos abaixo para criar o usuário utilizado pelo backend:

```sql
CREATE USER IF NOT EXISTS 'agroassist'@'localhost'
    IDENTIFIED BY 'agroassist_dev';

GRANT SELECT, INSERT, UPDATE, DELETE ON agroassist.*
    TO 'agroassist'@'localhost';
```

`agroassist_dev` é uma senha fictícia para desenvolvimento local. A configuração padrão do backend já usa estes valores:

| Configuração         | Valor            |
| -------------------- | ---------------- |
| Host                 | `localhost`      |
| Porta                | `3306`           |
| Banco                | `agroassist`     |
| Usuário da aplicação | `agroassist`     |
| Senha da aplicação   | `agroassist_dev` |

Se o usuário `agroassist` já existir, o comando `CREATE USER IF NOT EXISTS` mantém a senha atual dele. Nesse caso, informe essa senha em `DB_PASSWORD` ao executar o backend.

### Alternativa pelo cliente de linha de comando

Para fazer a mesma configuração sem o Workbench, entre no cliente MySQL:

```powershell
mysql -u root -p
```

Se o Windows informar que `mysql` não é reconhecido, abra o **MySQL Command Line Client** pelo menu Iniciar ou execute o programa pelo caminho completo no PowerShell:

```powershell
& "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe" -u root -p
```

Esse é o caminho padrão da instalação MSI; ajuste-o caso tenha instalado o MySQL em outra pasta. Informe a senha de `root` quando for solicitada.

No prompt `mysql>`, execute o script usando o caminho da pasta em que você extraiu o projeto. Exemplo:

```sql
SOURCE C:/projetos/agroassist/database/schema.sql;
```

Substitua `C:/projetos/agroassist` pelo seu caminho real. Em seguida, execute os mesmos comandos `CREATE USER` e `GRANT` apresentados acima. Para sair do cliente, use `EXIT;`.

O projeto usa `spring.jpa.hibernate.ddl-auto=validate`: o Hibernate confere as tabelas. Por isso, execute o `schema.sql` **antes** de iniciar o backend. O script não apaga registros existentes. O nome padrão do banco é `agroassist`; se alterar `DB_NAME`, ajuste também `CREATE DATABASE` e `USE` no script.

## 3. Executar o backend no IntelliJ

1. No IntelliJ, abra `backend/pom.xml` como projeto Maven e aguarde o carregamento das dependências.
2. Confira se o projeto usa **JDK 26**.
3. Abra `backend/src/main/java/br/edu/agroassist/AgroassistApplication.java`.
4. Clique no botão de execução ao lado do método `main` e execute **AgroassistApplication**.

Com o MySQL iniciado e o banco preparado, a API fica em **http://localhost:8080/api**. Abra **http://localhost:8080/api/dados** no navegador para conferir a conexão e os dados.

Na primeira inicialização com a tabela de usuários vazia, o backend carrega `database/seed.sql`. As próximas inicializações preservam os registros. O Maven inclui esse arquivo no classpath; não é preciso executar o seed manualmente.

### Se sua porta ou senha for diferente

No IntelliJ, abra **Run → Edit Configurations**, selecione a configuração de **AgroassistApplication** e preencha **Environment variables** com os valores que precisam mudar. Caso o campo esteja oculto, habilite-o em **Modify options**.

Exemplo para um MySQL na porta `3307`, com outra senha:

```text
DB_PORT=3307;DB_PASSWORD=sua_senha_local
```

As variáveis disponíveis são `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` e `SERVER_PORT`. Os padrões estão em `backend/src/main/resources/application.properties`. Para a configuração da seção anterior, não é necessário definir nenhuma variável.

Use variáveis locais para senhas reais; não as adicione ao repositório. Se trocar a porta HTTP do backend por `SERVER_PORT`, ajuste o destino do proxy em `frontend/vite.config.ts`.

### Alternativa pelo terminal

Em um terminal na raiz do projeto:

```powershell
cd backend
mvn spring-boot:run
```

Se precisar alterar a senha ou a porta, defina as variáveis **nesse mesmo terminal** antes de executar:

```powershell
$env:DB_PORT = "3307"
$env:DB_PASSWORD = "sua_senha_local"
mvn spring-boot:run
```

## 4. Executar o frontend

Abra **outro terminal**, na raiz do monorepo:

```bash
cd frontend
npm ci
npm run dev
```

Acesse **http://localhost:5173**. O Vite encaminha as chamadas `/api` para o backend na porta 8080. O arquivo `frontend/.env.example` documenta a URL base opcional.

O frontend aguarda os dados do servidor antes de abrir as telas e mostra uma mensagem com opção de tentar novamente se a conexão falhar. Não há fallback para banco simulado ou `localStorage`.

## 5. Testes e build

No diretório `backend`:

```bash
mvn test
mvn package
```

No IntelliJ, também é possível executar `test` e `package` pela janela **Maven → Lifecycle**.

Os testes unitários usam objetos reais do domínio e não precisam do MySQL. Cobrem consentimento, propriedade e cultura, atribuição de técnico, restrições de perfil, transições de status, avaliação única e histórico. Não são testes de getters e setters.

O pacote gerado pode ser executado com:

```bash
java -jar target/agroassist-1.0.0.jar
```

No diretório `frontend`:

```bash
npm run build
```

Esse comando verifica o TypeScript e gera `dist/`. Para a apresentação, `npm run dev` já atende ao fluxo documentado. `node_modules`, `target` e `dist` são gerados localmente e não fazem parte do ZIP de código-fonte.

### Validação desta entrega

- Backend compilado e empacotado com Java 26.0.2.1 e Maven 3.9.11; 25 testes de domínio aprovados.
- Frontend aprovado na verificação TypeScript e no build do Vite.
- API executada com MySQL 8.4.13, incluindo validações, restrições de perfil, transições, duas avaliações simultâneas e restauração dos exemplos.
- Onze verificações pelo navegador, cobrindo o fluxo de solicitação até avaliação, fotos, cadastros, situação do técnico e consulta dos registros; nenhum erro JavaScript.
- Backend reiniciado durante a conferência, preservando os mesmos registros, fotos e histórico no banco.

## Funcionalidades e roteiro de demonstração

As telas mantêm as áreas **Produtor**, **Técnico**, **Coordenação** e **Sobre o projeto**, além da página inicial, filtros, histórico, fotos, avaliação e opção de texto maior.

1. Entre em **Produtor** como Alexandre Gaia. Cadastre uma propriedade se desejar e abra uma solicitação para uma cultura cadastrada nela. A descrição é obrigatória; é possível anexar até três imagens.
2. Abra **Coordenação** e atribua a solicitação a um técnico ativo e habilitado na cultura. Na solicitação de exemplo **AGA-2026-0142**, de feijão, escolha **Daniel Ciriaco**.
3. Abra **Técnico**, selecione Daniel, escolha a solicitação e clique em **Iniciar atendimento**. Informe data, modalidade, diagnóstico e recomendação para concluir.
4. Volte ao **Produtor**, abra a solicitação e envie uma avaliação de 1 a 5. Confira o histórico completo.
5. Recarregue a página ou reinicie o backend: os registros continuam no MySQL.

A coordenação também pode cadastrar técnicos e ativar/inativar seus registros. O botão **Restaurar dados de exemplo** exige confirmação em dois cliques e **apaga os registros cadastrados nessa demonstração**, restaurando o conjunto inicial no MySQL.

## Regras de negócio

| Regra | Comportamento                                                                                                                                |
| ----- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| RN01  | A solicitação pertence ao produtor da propriedade e usa uma cultura cadastrada nela.                                                         |
| RN02  | Só um técnico ativo e habilitado na cultura pode ser atribuído. A reatribuição é permitida apenas antes do início.                           |
| RN03  | Só o técnico responsável inicia e conclui. A conclusão exige atendimento iniciado, diagnóstico e recomendação.                               |
| RN04  | Só o produtor autor pode avaliar, após a conclusão, uma única vez, com nota inteira de 1 a 5.                                                |
| RN05  | Cada transição acrescenta um evento com status, autor e horário; a aplicação não oferece edição ou exclusão de eventos.                      |
| RN06  | A tela do técnico oculta contato e nome da propriedade nas solicitações de outros técnicos, mantendo o comportamento demonstrativo original. |
| RN07  | O cadastro de produtor exige consentimento para o uso dos dados.                                                                             |

As regras de estado estão em métodos de domínio, como `Solicitacao.atribuir`, `iniciar`, `concluir` e `avaliar`. Os serviços coordenam a busca dos objetos, a transação e a gravação por JPA. As verificações do frontend apenas orientam o uso; o backend também verifica as operações.

## API REST

As mutações, com exceção do cadastro público de produtor, recebem o cabeçalho demonstrativo `X-Usuario-Id`. Exemplos: `u1` (Alexandre/produtor), `u3` (Daniel/técnico) e `u6` (coordenação). O backend busca esse usuário e verifica as regras do perfil informado.

| Método | Endpoint                             | Ação                                      |
| ------ | ------------------------------------ | ----------------------------------------- |
| GET    | `/api/dados`                         | Consultar os dados utilizados pelas telas |
| GET    | `/api/solicitacoes/{id}`             | Consultar uma solicitação e seu histórico |
| POST   | `/api/produtores`                    | Cadastrar produtor                        |
| POST   | `/api/propriedades`                  | Cadastrar propriedade do produtor         |
| POST   | `/api/solicitacoes`                  | Abrir solicitação                         |
| PATCH  | `/api/solicitacoes/{id}/atribuicao`  | Atribuir ou reatribuir técnico            |
| PATCH  | `/api/solicitacoes/{id}/inicio`      | Iniciar atendimento                       |
| POST   | `/api/solicitacoes/{id}/atendimento` | Registrar atendimento e concluir          |
| POST   | `/api/solicitacoes/{id}/avaliacao`   | Avaliar e encerrar o fluxo                |
| POST   | `/api/tecnicos`                      | Cadastrar técnico                         |
| PATCH  | `/api/tecnicos/{id}/situacao`        | Ativar ou inativar técnico                |
| POST   | `/api/demo/reiniciar`                | Restaurar os dados fictícios              |

Cadastros retornam **201**; consultas e atualizações, **200**; restauração, **204**. Dados inválidos retornam **400**; perfil ou ator incompatível, **403**; recurso inexistente, **404**; regra de transição ou conflito de integridade, **409**. Erros retornam JSON com `codigo`, `mensagem` e `em`.

Exemplo de abertura, com `Content-Type: application/json` e `X-Usuario-Id: u1`:

```json
{
  "propriedadeId": "p1",
  "culturaId": "c2",
  "estadio": "V5",
  "descricao": "Folhas com manchas na parte inferior da lavoura.",
  "fotos": []
}
```

## Modelagem, JPA e apresentação

Os sete conceitos principais são **Usuário, Propriedade, Cultura, Técnico, Solicitação, Atendimento e Avaliação**. A modelagem detalhada está em [database/modelo.md](database/modelo.md), e a implementação está em [database/schema.sql](database/schema.sql).

O mapeamento utiliza `@ManyToOne`, `@OneToOne` e `@ManyToMany` onde correspondem ao domínio, além de `@ElementCollection` para fotos e eventos do histórico. As tabelas possuem chaves estrangeiras, e atendimento e avaliação possuem vínculo único com a solicitação. Não é necessário usar todas as anotações de relacionamento apenas para demonstrá-las.

O caminho de uma operação é **React → HTTP/JSON → Controller → Service → Domain → Repository → JPA/Hibernate → MySQL**. O React atualiza os dados exibidos após a resposta da operação e uma nova consulta à API.

Comparado com JDBC manual, o JPA cuida do mapeamento entre objetos e linhas, dos parâmetros SQL, do acompanhamento de alterações nas entidades e da materialização dos relacionamentos. O Spring Data fornece as operações de repositório. A aplicação continua responsável pela modelagem, pelas regras e pelos limites das transações. O SQL de seed é usado apenas para os exemplos; os cadastros e fluxos da aplicação usam JPA.

## Escopo acadêmico preservado

O seletor de usuários é demonstrativo, **sem autenticação por login e senha**. O cabeçalho de usuário pode ser escolhido pelo cliente, e `/api/dados` fornece os dados agregados para as várias telas. A ocultação da RN06 é visual e não constitui proteção de acesso à API. Use os dados fictícios da atividade.

As fotos são armazenadas como `data:` URLs no MySQL; imagens enviadas pela interface são reduzidas para JPEG, e as imagens de exemplo são SVGs. O limite é de três fotos, com até 1.000.000 de caracteres por imagem. O campo orientativo de categoria da ocorrência permanece apenas na revisão do formulário, como no projeto original; a descrição do problema é persistida.

O projeto mantém um backend único e um frontend, com os recursos necessários ao desafio. O botão de restauração é destinado à demonstração. A opção `DEMO_ENABLED=false` desabilita a carga automática e a restauração dos exemplos; utilize-a somente após a carga inicial, pois a interface demonstrativa utiliza os perfis e as culturas previamente cadastrados.

## Solução de problemas

| Sintoma                                 | Conferir                                                                                                                            |
| --------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `release version 26 not supported`      | O Java informado por `mvn -v` precisa ser o JDK 26.                                                                                 |
| Erro de conexão com MySQL               | Banco iniciado, porta, usuário, senha e variáveis do terminal.                                                                      |
| `Schema-validation: missing table`      | Execute `database/schema.sql` antes de iniciar o backend.                                                                           |
| Frontend mostra erro de conexão         | Backend na porta 8080 e proxy do Vite correspondente.                                                                               |
| `mysql` não é reconhecido no PowerShell | Use o MySQL Command Line Client no menu Iniciar ou o caminho completo de `mysql.exe` mostrado na seção 2.                           |
| MySQL local não inicia                  | Confira o serviço em `services.msc` e conclua a configuração pelo MySQL Configurator.                                               |
| Exemplos não reaparecem ao reiniciar    | Esse é o comportamento esperado: o seed só é automático com usuários vazios. Use a restauração confirmada para voltar aos exemplos. |

## Referências técnicas

- [Spring Boot 4.0 — requisitos e compatibilidade Java](https://docs.spring.io/spring-boot/4.0/system-requirements.html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
- [MySQL 8.4 — instalação e configuração no Windows](https://dev.mysql.com/doc/refman/8.4/en/windows-installation.html)
- [MySQL 8.4 — serviço do Windows](https://dev.mysql.com/doc/refman/8.4/en/windows-start-service.html)
