import { useCallback, useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { api } from './api';
import './App.css';

function formatarDataHora(valor) {
  return new Date(valor).toLocaleString("pt-BR");
}

function useLista(recurso) {
  const [itens, setItens] = useState([]);

  const recarregar = useCallback(async () => {
    try {
      setItens(await api.listar(recurso));
    } catch (excecao) {
      toast.error(excecao.message);
    }
  }, [recurso]);

  useEffect(() => {
    recarregar();
  }, [recarregar]);

  return { itens, recarregar };
}

// toda ação do usuário passa por aqui: pergunta, executa, mostra o resultado em um toast (react-toastify) e recarrega a lista
// a página só precisa colocar o `dialogo` no JSX
function useAcaoConfirmada(aoConcluir) {
  const [pendente, setPendente] = useState(null);

  function solicitar(descricao, executar, mensagemSucesso) {
    setPendente({ descricao, executar, mensagemSucesso });
  }

  async function confirmar() {
    const { executar, mensagemSucesso } = pendente;
    setPendente(null);
    try {
      await executar();
      toast.success(mensagemSucesso);
      await aoConcluir();
    } catch (excecao) {
      toast.error(excecao.message);
    }
  }

  const dialogo = pendente && (
    <ConfirmDialog
      descricao={pendente.descricao}
      aoConfirmar={confirmar}
      aoCancelar={() => setPendente(null)}
    />
  );

  return { solicitar, dialogo };
}

// variante muda a cor: primario, secundario ou perigo
function Botao({ variante = "primario", tipo = "button", children, ...props }) {
  return (
    <button type={tipo} className={"botao botao--" + variante} {...props}>
      {children}
    </button>
  );
}

// se passar `opcoes` (lista de { valor, texto }) vira um select, senão é um input normal
function Campo({ rotulo, id, opcoes, ...props }) {
  return (
    <div className="campo">
      <label className="campo__rotulo" htmlFor={id}>
        {rotulo}
      </label>
      {opcoes ? (
        <select id={id} className="campo__controle" {...props}>
          <option value="">Selecione</option>
          {opcoes.map((opcao) => (
            <option key={opcao.valor} value={opcao.valor}>
              {opcao.texto}
            </option>
          ))}
        </select>
      ) : (
        <input id={id} className="campo__controle" {...props} />
      )}
    </div>
  );
}

function Formulario({ aoEnviar, aoCancelar, textoEnviar, children }) {
  function tratarEnvio(evento) {
    evento.preventDefault();
    aoEnviar();
  }

  return (
    <form onSubmit={tratarEnvio}>
      <div className="formulario__campos">{children}</div>
      <div className="formulario__acoes">
        <Botao tipo="submit">{textoEnviar}</Botao>
        {aoCancelar && (
          <Botao variante="secundario" onClick={aoCancelar}>
            Cancelar edição
          </Botao>
        )}
      </div>
    </form>
  );
}

// cada coluna tem um `titulo` e um `render` que recebe a linha
// as linhas precisam ter `id` (usado como key)
function Tabela({ colunas, linhas, mensagemVazia }) {
  if (linhas.length === 0) {
    return <p className="tabela__vazia">{mensagemVazia}</p>;
  }

  return (
    <div className="tabela__rolagem">
      <table className="tabela">
        <thead>
          <tr>
            {colunas.map((coluna) => (
              <th key={coluna.titulo}>{coluna.titulo}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {linhas.map((linha) => (
            <tr key={linha.id}>
              {colunas.map((coluna) => (
                <td key={coluna.titulo}>{coluna.render(linha)}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function Acoes({ children }) {
  return <div className="tabela__acoes">{children}</div>;
}

function ConfirmDialog({ descricao, aoConfirmar, aoCancelar }) {
  return (
    <div className="confirmacao">
      <div className="confirmacao__caixa" role="dialog" aria-modal="true">
        <h2 className="confirmacao__titulo">Confirmação</h2>
        <p className="confirmacao__pergunta">
          Realmente deseja prosseguir com a ação: {descricao}?
        </p>
        <div className="confirmacao__acoes">
          <Botao variante="secundario" onClick={aoCancelar}>
            Cancelar
          </Botao>
          <Botao onClick={aoConfirmar}>Confirmar</Botao>
        </div>
      </div>
    </div>
  );
}

// variante muda a cor: neutra, sucesso, alerta ou perigo
function Etiqueta({ variante = "neutra", children }) {
  return <span className={"etiqueta etiqueta--" + variante}>{children}</span>;
}

function Secao({ titulo, children }) {
  return (
    <section className="secao">
      <h2 className="secao__titulo">{titulo}</h2>
      {children}
    </section>
  );
}

function Navegacao({ abas, abaAtiva, aoSelecionar }) {
  return (
    <nav className="navegacao">
      {abas.map((aba) => (
        <button
          key={aba.id}
          type="button"
          className={"navegacao__item" + (aba.id === abaAtiva ? " navegacao__item--ativo" : "")}
          onClick={() => aoSelecionar(aba.id)}
        >
          {aba.titulo}
        </button>
      ))}
    </nav>
  );
}

function CategoriasPage() {
  const { itens: categorias, recarregar } = useLista("categorias");
  const { solicitar, dialogo } = useAcaoConfirmada(recarregar);
  const [nome, setNome] = useState("");
  const [editandoId, setEditandoId] = useState(null);

  function limparFormulario() {
    setNome("");
    setEditandoId(null);
  }

  function salvar() {
    const dados = { nome };

    if (editandoId === null) {
      solicitar(
        `cadastrar a categoria "${nome}"`,
        async () => {
          await api.criar("categorias", dados);
          limparFormulario();
        },
        "Categoria cadastrada."
      );
      return;
    }

    solicitar(
      `salvar as alterações da categoria "${nome}"`,
      async () => {
        await api.atualizar("categorias", editandoId, dados);
        limparFormulario();
      },
      "Categoria atualizada."
    );
  }

  function editar(categoria) {
    setEditandoId(categoria.id);
    setNome(categoria.nome);
  }

  function excluir(categoria) {
    solicitar(
      `excluir a categoria "${categoria.nome}"`,
      async () => {
        await api.remover("categorias", categoria.id);
        if (editandoId === categoria.id) {
          limparFormulario();
        }
      },
      "Categoria excluída."
    );
  }

  const colunas = [
    { titulo: "Nome", render: (categoria) => categoria.nome },
    {
      titulo: "Ações",
      render: (categoria) => (
        <Acoes>
          <Botao variante="secundario" onClick={() => editar(categoria)}>
            Editar
          </Botao>
          <Botao variante="perigo" onClick={() => excluir(categoria)}>
            Excluir
          </Botao>
        </Acoes>
      ),
    },
  ];

  return (
    <>

      <Secao titulo={editandoId === null ? "Nova categoria" : "Editar categoria"}>
        <Formulario
          aoEnviar={salvar}
          aoCancelar={editandoId === null ? null : limparFormulario}
          textoEnviar={editandoId === null ? "Cadastrar" : "Salvar alterações"}
        >
          <Campo
            id="categoria-nome"
            rotulo="Nome"
            value={nome}
            onChange={(evento) => setNome(evento.target.value)}
            maxLength={80}
            required
          />
        </Formulario>
      </Secao>

      <Secao titulo="Categorias">
        <Tabela colunas={colunas} linhas={categorias} mensagemVazia="Nenhuma categoria cadastrada." />
      </Secao>

      {dialogo}
    </>
  );
}

const PRODUTO_VAZIO = { codigo: "", nome: "", categoriaId: "", estoqueMinimo: "0" };

function ProdutosPage() {
  const { itens: produtos, recarregar } = useLista("produtos");
  const { itens: categorias } = useLista("categorias");
  const { solicitar, dialogo } = useAcaoConfirmada(recarregar);
  const [formulario, setFormulario] = useState(PRODUTO_VAZIO);
  const [editandoId, setEditandoId] = useState(null);

  function alterarCampo(campo) {
    return (evento) => setFormulario({ ...formulario, [campo]: evento.target.value });
  }

  function limparFormulario() {
    setFormulario(PRODUTO_VAZIO);
    setEditandoId(null);
  }

  function salvar() {
    const dados = {
      ...formulario,
      categoriaId: Number(formulario.categoriaId),
      estoqueMinimo: Number(formulario.estoqueMinimo),
    };
    const nomeProduto = `"${formulario.codigo} - ${formulario.nome}"`;

    if (editandoId === null) {
      solicitar(
        `cadastrar o produto ${nomeProduto}`,
        async () => {
          await api.criar("produtos", dados);
          limparFormulario();
        },
        "Produto cadastrado."
      );
      return;
    }

    solicitar(
      `salvar as alterações do produto ${nomeProduto}`,
      async () => {
        await api.atualizar("produtos", editandoId, dados);
        limparFormulario();
      },
      "Produto atualizado."
    );
  }

  function editar(produto) {
    setEditandoId(produto.id);
    setFormulario({
      codigo: produto.codigo,
      nome: produto.nome,
      categoriaId: String(produto.categoriaId),
      estoqueMinimo: String(produto.estoqueMinimo),
    });
  }

  function alterarSituacao(produto) {
    const situacao = produto.ativo ? "inativacao" : "ativacao";
    const verbo = produto.ativo ? "inativar" : "ativar";

    solicitar(
      `${verbo} o produto "${produto.codigo} - ${produto.nome}"`,
      () => api.alterarSituacao("produtos", produto.id, situacao),
      produto.ativo ? "Produto inativado." : "Produto ativado."
    );
  }

  function excluir(produto) {
    solicitar(
      `excluir o produto "${produto.codigo} - ${produto.nome}"`,
      async () => {
        await api.remover("produtos", produto.id);
        if (editandoId === produto.id) {
          limparFormulario();
        }
      },
      "Produto excluído."
    );
  }

  const opcoesCategoria = categorias.map((categoria) => ({
    valor: categoria.id,
    texto: categoria.nome,
  }));

  const colunas = [
    { titulo: "Código", render: (produto) => produto.codigo },
    { titulo: "Nome", render: (produto) => produto.nome },
    { titulo: "Categoria", render: (produto) => produto.categoria },
    {
      titulo: "Saldo",
      render: (produto) => (
        <div className="tabela__saldo">
          <span>{produto.quantidade}</span>
          {produto.estoqueBaixo && <Etiqueta variante="alerta">Estoque baixo</Etiqueta>}
        </div>
      ),
    },
    { titulo: "Mínimo", render: (produto) => produto.estoqueMinimo },
    {
      titulo: "Situação",
      render: (produto) => (
        <Etiqueta variante={produto.ativo ? "sucesso" : "neutra"}>
          {produto.ativo ? "Ativo" : "Inativo"}
        </Etiqueta>
      ),
    },
    {
      titulo: "Ações",
      render: (produto) => (
        <Acoes>
          <Botao variante="secundario" onClick={() => editar(produto)}>
            Editar
          </Botao>
          <Botao variante="secundario" onClick={() => alterarSituacao(produto)}>
            {produto.ativo ? "Inativar" : "Ativar"}
          </Botao>
          <Botao variante="perigo" onClick={() => excluir(produto)}>
            Excluir
          </Botao>
        </Acoes>
      ),
    },
  ];

  return (
    <>

      <Secao titulo={editandoId === null ? "Novo produto" : "Editar produto"}>
        <Formulario
          aoEnviar={salvar}
          aoCancelar={editandoId === null ? null : limparFormulario}
          textoEnviar={editandoId === null ? "Cadastrar" : "Salvar alterações"}
        >
          <Campo
            id="produto-codigo"
            rotulo="Código"
            value={formulario.codigo}
            onChange={alterarCampo("codigo")}
            maxLength={30}
            disabled={editandoId !== null}
            required
          />
          <Campo
            id="produto-nome"
            rotulo="Nome"
            value={formulario.nome}
            onChange={alterarCampo("nome")}
            maxLength={120}
            required
          />
          <Campo
            id="produto-categoria"
            rotulo="Categoria"
            opcoes={opcoesCategoria}
            value={formulario.categoriaId}
            onChange={alterarCampo("categoriaId")}
            required
          />
          <Campo
            id="produto-minimo"
            rotulo="Estoque mínimo"
            type="number"
            min="0"
            value={formulario.estoqueMinimo}
            onChange={alterarCampo("estoqueMinimo")}
            required
          />
        </Formulario>
      </Secao>

      <Secao titulo="Produtos">
        <Tabela colunas={colunas} linhas={produtos} mensagemVazia="Nenhum produto cadastrado." />
      </Secao>

      {dialogo}
    </>
  );
}

const MOVIMENTACAO_VAZIA = { produtoId: "", tipo: "ENTRADA", quantidade: "", motivo: "" };

const OPCOES_TIPO = [
  { valor: "ENTRADA", texto: "Entrada" },
  { valor: "SAIDA", texto: "Saída" },
];

function MovimentacoesPage() {
  const { itens: movimentacoes, recarregar: recarregarMovimentacoes } = useLista("movimentacoes");
  const { itens: produtos, recarregar: recarregarProdutos } = useLista("produtos");
  const { solicitar, dialogo } = useAcaoConfirmada(() =>
    Promise.all([recarregarMovimentacoes(), recarregarProdutos()])
  );
  const [formulario, setFormulario] = useState(MOVIMENTACAO_VAZIA);

  function alterarCampo(campo) {
    return (evento) => setFormulario({ ...formulario, [campo]: evento.target.value });
  }

  function registrar() {
    const dados = {
      produtoId: Number(formulario.produtoId),
      tipo: formulario.tipo,
      quantidade: Number(formulario.quantidade),
      motivo: formulario.motivo,
    };
    const produto = produtos.find((item) => item.id === dados.produtoId);
    const tipoTexto = dados.tipo === "ENTRADA" ? "entrada" : "saída";

    solicitar(
      `registrar ${tipoTexto} de ${dados.quantidade} un. do produto "${produto.codigo} - ${produto.nome}"`,
      async () => {
        await api.criar("movimentacoes", dados);
        setFormulario(MOVIMENTACAO_VAZIA);
      },
      "Movimentação registrada."
    );
  }

  // produto inativo não pode ser movimentado, então nem aparece aqui
  const opcoesProduto = produtos
    .filter((produto) => produto.ativo)
    .map((produto) => ({
      valor: produto.id,
      texto: `${produto.codigo} - ${produto.nome} (saldo: ${produto.quantidade})`,
    }));

  const colunas = [
    { titulo: "Data", render: (movimentacao) => formatarDataHora(movimentacao.dataHora) },
    { titulo: "Produto", render: (movimentacao) => movimentacao.produto },
    {
      titulo: "Tipo",
      render: (movimentacao) => (
        <Etiqueta variante={movimentacao.tipo === "ENTRADA" ? "sucesso" : "alerta"}>
          {movimentacao.tipo === "ENTRADA" ? "Entrada" : "Saída"}
        </Etiqueta>
      ),
    },
    { titulo: "Quantidade", render: (movimentacao) => movimentacao.quantidade },
    { titulo: "Motivo", render: (movimentacao) => movimentacao.motivo || "-" },
  ];

  return (
    <>

      <Secao titulo="Nova movimentação">
        <Formulario aoEnviar={registrar} textoEnviar="Registrar">
          <Campo
            id="movimentacao-produto"
            rotulo="Produto"
            opcoes={opcoesProduto}
            value={formulario.produtoId}
            onChange={alterarCampo("produtoId")}
            required
          />
          <Campo
            id="movimentacao-tipo"
            rotulo="Tipo"
            opcoes={OPCOES_TIPO}
            value={formulario.tipo}
            onChange={alterarCampo("tipo")}
            required
          />
          <Campo
            id="movimentacao-quantidade"
            rotulo="Quantidade"
            type="number"
            min="1"
            value={formulario.quantidade}
            onChange={alterarCampo("quantidade")}
            required
          />
          <Campo
            id="movimentacao-motivo"
            rotulo="Motivo (opcional)"
            value={formulario.motivo}
            onChange={alterarCampo("motivo")}
            maxLength={200}
          />
        </Formulario>
      </Secao>

      <Secao titulo="Histórico de movimentações">
        <Tabela
          colunas={colunas}
          linhas={movimentacoes}
          mensagemVazia="Nenhuma movimentação registrada."
        />
      </Secao>

      {dialogo}
    </>
  );
}

// essa tela só lista, de propósito não tem botão de excluir log
function LogsPage() {
  const { itens: logs, recarregar } = useLista("logs");

  const colunas = [
    { titulo: "Data", render: (log) => formatarDataHora(log.dataHora) },
    { titulo: "Ação", render: (log) => <Etiqueta>{log.acao}</Etiqueta> },
    { titulo: "Entidade", render: (log) => log.entidade },
    { titulo: "Descrição", render: (log) => log.descricao },
  ];

  return (
    <>

      <Secao titulo="Logs de auditoria">
        <p className="logs__nota">
          Registros somente leitura. A exclusão só pode ser feita diretamente no banco de dados.
        </p>
        <Botao variante="secundario" onClick={recarregar}>
          Atualizar
        </Botao>
        <Tabela colunas={colunas} linhas={logs} mensagemVazia="Nenhum log registrado." />
      </Secao>
    </>
  );
}

const ABAS = [
  { id: "produtos", titulo: "Produtos", pagina: ProdutosPage },
  { id: "categorias", titulo: "Categorias", pagina: CategoriasPage },
  { id: "movimentacoes", titulo: "Movimentações", pagina: MovimentacoesPage },
  { id: "logs", titulo: "Logs", pagina: LogsPage },
];

function App() {
  const [abaAtiva, setAbaAtiva] = useState("produtos");
  const Pagina = ABAS.find((aba) => aba.id === abaAtiva).pagina;

  return (
    <main className="aplicacao">
      <ToastContainer position="top-right" autoClose={4000} />
      <h1 className="aplicacao__titulo">Controle de Estoque</h1>
      <Navegacao abas={ABAS} abaAtiva={abaAtiva} aoSelecionar={setAbaAtiva} />
      <Pagina />
    </main>
  );
}

export default App;
