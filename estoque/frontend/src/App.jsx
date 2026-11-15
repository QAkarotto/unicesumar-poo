import { useEffect, useMemo, useRef, useState } from "react";
import { api } from "./api.js";

const brl = (v) => Number(v).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
const VAZIO = { sku: "", nome: "", preco: "", estoqueMinimo: 0, categoriaId: "" };
const SEM_ERRO = { campos: {}, geral: "" };

// Converte o erro da API em { campos: {campo: msg}, geral: msg } para mostrar junto do campo certo.
const parse = (e) => {
  const campos = { ...(e.campos || {}) };
  let geral = "";
  if (e.campo) campos[e.campo] = e.message;
  else if (!Object.keys(campos).length) geral = e.message;
  return { campos, geral };
};

const Erro = ({ texto }) => (texto ? <span className="erro-campo" role="alert">{texto}</span> : null);

function Modal({ titulo, onClose, children }) {
  const ref = useRef(null);
  useEffect(() => { ref.current?.showModal(); }, []);
  return (
    <dialog ref={ref} onClose={onClose} onClick={(e) => e.target === ref.current && ref.current.close()}>
      <h2>{titulo}</h2>
      {children}
    </dialog>
  );
}

export default function App() {
  const [aba, setAba] = useState("produtos");
  const [categorias, setCategorias] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [movs, setMovs] = useState([]);
  const [busca, setBusca] = useState("");
  const [soBaixo, setSoBaixo] = useState(false);
  const [falhaCarga, setFalhaCarga] = useState("");

  const [modal, setModal] = useState(null);
  const [form, setForm] = useState(VAZIO);
  const [errP, setErrP] = useState(SEM_ERRO);

  const [mov, setMov] = useState({ produtoId: "", tipo: "ENTRADA", quantidade: 1 });
  const [errM, setErrM] = useState(SEM_ERRO);
  const [okM, setOkM] = useState("");
  const qtdRef = useRef(null);

  const [nomeCat, setNomeCat] = useState("");
  const [errC, setErrC] = useState(SEM_ERRO);
  const [okC, setOkC] = useState("");

  const [erroItem, setErroItem] = useState(null);

  const carregar = async () => {
    try {
      const [c, p, m] = await Promise.all([api.categorias(), api.produtos(), api.movimentacoes()]);
      setCategorias(c); setProdutos(p); setMovs(m); setFalhaCarga("");
    } catch (e) { setFalhaCarga(e.message); }
  };
  useEffect(() => { carregar(); }, []);

  const ativos = produtos.filter((p) => p.ativo);
  const baixos = ativos.filter((p) => p.abaixoDoMinimo);
  const valor = ativos.reduce((s, p) => s + p.preco * p.quantidade, 0);
  const sel = produtos.find((p) => String(p.id) === String(mov.produtoId));
  const lista = useMemo(() => produtos.filter((p) =>
    (!soBaixo || (p.ativo && p.abaixoDoMinimo)) &&
    `${p.nome} ${p.sku} ${p.categoria}`.toLowerCase().includes(busca.toLowerCase())), [produtos, busca, soBaixo]);

  // ---- produto (janela) ----
  const novoProduto = () => { setForm(VAZIO); setErrP(SEM_ERRO); setModal({}); };
  const editar = (p) => {
    setForm({ sku: p.sku, nome: p.nome, preco: p.preco, estoqueMinimo: p.estoqueMinimo,
      categoriaId: categorias.find((c) => c.nome === p.categoria)?.id ?? "" });
    setErrP(SEM_ERRO); setModal({ item: p });
  };
  const salvar = async (e) => {
    e.preventDefault(); setErrP(SEM_ERRO);
    const corpo = { ...form, preco: Number(form.preco), estoqueMinimo: Number(form.estoqueMinimo), categoriaId: Number(form.categoriaId) };
    try {
      if (modal.item) await api.atualizarProduto(modal.item.id, corpo); else await api.criarProduto(corpo);
      await carregar(); setModal(null);
    } catch (x) { setErrP(parse(x)); }
  };
  const campo = (k, props) => (
    <>
      <input className={errP.campos[k] ? "invalido" : ""} value={form[k]} onChange={(e) => setForm({ ...form, [k]: e.target.value })} {...props} />
      <Erro texto={errP.campos[k]} />
    </>
  );

  // ---- movimentação (painel lateral) ----
  const usar = (p, tipo) => {
    setMov({ produtoId: String(p.id), tipo, quantidade: 1 }); setErrM(SEM_ERRO); setOkM("");
    qtdRef.current?.focus();
  };
  const registrar = async (e) => {
    e.preventDefault(); setErrM(SEM_ERRO); setOkM("");
    try {
      await api.movimentar({ produtoId: Number(mov.produtoId), tipo: mov.tipo, quantidade: Number(mov.quantidade) });
      await carregar();
      setOkM(mov.tipo === "ENTRADA" ? "Entrada registrada." : "Saída registrada.");
    } catch (x) { setErrM(parse(x)); }
  };

  // ---- categoria ----
  const addCategoria = async (e) => {
    e.preventDefault(); setErrC(SEM_ERRO); setOkC("");
    try { await api.criarCategoria(nomeCat.trim()); setNomeCat(""); setOkC("Categoria criada."); await carregar(); }
    catch (x) { setErrC(parse(x)); }
  };

  // ---- ações em linha (o erro aparece na própria linha) ----
  const acaoItem = (chave, fn, confirmar) => async () => {
    if (confirmar && !window.confirm(confirmar)) return;
    setErroItem(null);
    try { await fn(); await carregar(); } catch (x) { setErroItem({ chave, texto: x.message }); }
  };

  return (
    <div className="app">
      <aside>
        <h1>Estoque</h1>
        <nav>
          {[["produtos", "Produtos"], ["categorias", "Categorias"]].map(([id, nome]) => (
            <button key={id} className={aba === id ? "ativa" : ""} onClick={() => setAba(id)}>{nome}</button>
          ))}
        </nav>
      </aside>

      <main>
        {falhaCarga && <p className="erro-faixa" role="alert">{falhaCarga}</p>}
        <section className="resumo">
          <div><b>{ativos.length}</b><span>produtos ativos</span></div>
          <div><b>{brl(valor)}</b><span>valor em estoque</span></div>
          <button className={baixos.length ? "alerta" : ""} disabled={!baixos.length}
            onClick={() => { setAba("produtos"); setSoBaixo(true); }}>
            <b>{baixos.length}</b><span>abaixo do mínimo</span>
          </button>
        </section>

        {aba === "produtos" && (
          <section>
            <div className="barra">
              <input type="search" placeholder="Buscar por nome, SKU ou categoria" value={busca} onChange={(e) => setBusca(e.target.value)} />
              <label><input type="checkbox" checked={soBaixo} onChange={(e) => setSoBaixo(e.target.checked)} /> Só abaixo do mínimo</label>
              <button className="primario" onClick={novoProduto} disabled={!categorias.length}>Novo produto</button>
            </div>
            {!categorias.length && <p className="vazio">Crie uma categoria na aba Categorias para cadastrar produtos.</p>}
            <div className="rolagem">
              <table>
                <thead><tr><th>Produto</th><th>Categoria</th><th className="num">Preço</th><th className="num">Estoque</th><th>Ações</th></tr></thead>
                <tbody>
                  {lista.map((p) => (
                    <tr key={p.id} className={p.ativo ? "" : "inativo"}>
                      <td>{p.nome}<small>{p.sku}</small>
                        {erroItem?.chave === "p" + p.id && <Erro texto={erroItem.texto} />}</td>
                      <td>{p.categoria}</td>
                      <td className="num">{brl(p.preco)}</td>
                      <td className="num">
                        <span className={"pill " + (p.ativo && p.abaixoDoMinimo ? "baixo" : "ok")}>{p.quantidade}</span>
                        <small>{p.ativo ? `mínimo ${p.estoqueMinimo}` : "inativo"}</small>
                      </td>
                      <td className="acoes">
                        {p.ativo && <>
                          <button onClick={() => usar(p, "ENTRADA")}>Entrada</button>
                          <button onClick={() => usar(p, "SAIDA")}>Saída</button>
                          <button className="link" onClick={() => editar(p)}>Editar</button>
                          <button className="link" onClick={acaoItem("p" + p.id, () => api.desativarProduto(p.id), `Desativar ${p.nome}?`)}>Desativar</button>
                        </>}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {!lista.length && <p className="vazio">Nenhum produto encontrado.</p>}
            </div>
          </section>
        )}

        {aba === "categorias" && (
          <section>
            <form onSubmit={addCategoria} className="campo-unico">
              <div className="barra">
                <input className={errC.campos.nome ? "invalido" : ""} placeholder="Nome da nova categoria" value={nomeCat}
                  onChange={(e) => { setNomeCat(e.target.value); setErrC(SEM_ERRO); setOkC(""); }} required />
                <button className="primario">Adicionar categoria</button>
              </div>
              <Erro texto={errC.campos.nome || errC.geral} />
              {okC && <span className="ok-campo" role="status">{okC}</span>}
            </form>
            <ul className="cats">
              {categorias.map((c) => (
                <li key={c.id}>
                  <span>{c.nome}{erroItem?.chave === "c" + c.id && <Erro texto={erroItem.texto} />}</span>
                  <button className="link" onClick={acaoItem("c" + c.id, () => api.excluirCategoria(c.id), `Excluir ${c.nome}?`)}>Excluir</button>
                </li>
              ))}
            </ul>
            {!categorias.length && <p className="vazio">Nenhuma categoria ainda.</p>}
          </section>
        )}
      </main>

      <div className="painel">
        <section>
          <h2>Movimentar estoque</h2>
          <form onSubmit={registrar} className="grade">
            <label>Produto
              <select value={mov.produtoId} onChange={(e) => { setMov({ ...mov, produtoId: e.target.value }); setErrM(SEM_ERRO); setOkM(""); }} required>
                <option value="">Escolha…</option>
                {ativos.map((p) => <option key={p.id} value={p.id}>{p.sku} · {p.nome}</option>)}
              </select>
            </label>
            {sel && <p className="dica">Saldo atual: <b>{sel.quantidade}</b></p>}
            <div className="seg" role="group" aria-label="Tipo de movimentação">
              {[["ENTRADA", "Entrada"], ["SAIDA", "Saída"]].map(([v, nome]) => (
                <button type="button" key={v} className={mov.tipo === v ? "ativo" : ""}
                  onClick={() => { setMov({ ...mov, tipo: v }); setErrM(SEM_ERRO); setOkM(""); }}>{nome}</button>
              ))}
            </div>
            <label>Quantidade
              <input ref={qtdRef} type="number" min="1" className={errM.campos.quantidade ? "invalido" : ""} value={mov.quantidade}
                onChange={(e) => { setMov({ ...mov, quantidade: e.target.value }); setErrM(SEM_ERRO); setOkM(""); }} required />
              <Erro texto={errM.campos.quantidade} />
            </label>
            <button className="primario">{mov.tipo === "ENTRADA" ? "Registrar entrada" : "Registrar saída"}</button>
            <Erro texto={errM.geral || errM.campos.produtoId} />
            {okM && <span className="ok-campo" role="status">{okM}</span>}
          </form>
        </section>

        <section>
          <h2>Histórico</h2>
          <ol className="historico">
            {movs.map((m) => (
              <li key={m.id}>
                <span className={"tag " + (m.tipo === "ENTRADA" ? "entrada" : "saida")}>
                  {m.tipo === "ENTRADA" ? "+" : "−"}{m.quantidade}
                </span>
                <div><b>{m.produto}</b><small>{m.tipo === "ENTRADA" ? "Entrada" : "Saída"} · {new Date(m.dataHora).toLocaleString("pt-BR")}</small></div>
              </li>
            ))}
          </ol>
          {!movs.length && <p className="vazio">Ainda não há movimentações.</p>}
        </section>
      </div>

      {modal && (
        <Modal titulo={modal.item ? "Editar produto" : "Novo produto"} onClose={() => setModal(null)}>
          <form onSubmit={salvar} className="grade">
            <label>SKU{campo("sku", { required: true, disabled: !!modal.item })}</label>
            <label>Nome{campo("nome", { required: true })}</label>
            <label>Preço (R$){campo("preco", { type: "number", step: "0.01", min: "0", required: true })}</label>
            <label>Estoque mínimo{campo("estoqueMinimo", { type: "number", min: "0", required: true })}</label>
            <label>Categoria
              <select className={errP.campos.categoriaId ? "invalido" : ""} value={form.categoriaId}
                onChange={(e) => setForm({ ...form, categoriaId: e.target.value })} required>
                <option value="">Escolha…</option>
                {categorias.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
              </select>
              <Erro texto={errP.campos.categoriaId} />
            </label>
            <Erro texto={errP.geral} />
            <div className="rodape"><button type="button" onClick={() => setModal(null)}>Cancelar</button><button className="primario">Salvar produto</button></div>
          </form>
        </Modal>
      )}
    </div>
  );
}
