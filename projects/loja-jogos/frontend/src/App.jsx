import React, { useState, useEffect } from 'react';

export default function App() {
  const [aba, setAba] = useState('jogos');
  const [jogos, setJogos] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [pedidos, setPedidos] = useState([]);

  // Estados de filtros para a biblioteca de jogos
  const [termoBusca, setTermoBusca] = useState('');
  const [filtroPlataforma, setFiltroPlataforma] = useState('');
  const [precoMaximo, setPrecoMaximo] = useState('');

  const [novoJogo, setNovoJogo] = useState({ titulo: '', plataforma: '', preco: '', estoque: '' });
  const [editandoJogoId, setEditandoJogoId] = useState(null);  const [novoCliente, setNovoCliente] = useState({ nome: '', email: '' });
  const [novoPedido, setNovoPedido] = useState({ clienteId: '', jogoId: '', quantidade: '' });
  
  const [mensagemSucesso, setMensagemSucesso] = useState('');

  const API_JOGOS = 'http://localhost:8080/api/jogos';
  const API_CLIENTES = 'http://localhost:8080/api/clientes';
  const API_PEDIDOS = 'http://localhost:8080/api/pedidos';

  useEffect(() => {
    carregarDados();
  }, [aba]);

  const carregarDados = () => {
    fetch(API_JOGOS).then(res => res.json()).then(setJogos).catch(console.error);
    fetch(API_CLIENTES).then(res => res.json()).then(setClientes).catch(console.error);
    fetch(API_PEDIDOS).then(res => res.json()).then(setPedidos).catch(console.error);
  };
  const mostrarAlertaSucesso = (msg) => {
    setMensagemSucesso(msg);
    setTimeout(() => setMensagemSucesso(''), 3500);
  };

  const salvarJogo = (e) => {
    e.preventDefault();
    const url = editandoJogoId ? `${API_JOGOS}/${editandoJogoId}` : API_JOGOS;
    const metodo = editandoJogoId ? 'PUT' : 'POST';
    fetch(url, {
      method: metodo,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({       titulo: novoJogo.titulo,
        plataforma: novoJogo.plataforma,
        preco: Number(novoJogo.preco),
        estoque: Number(novoJogo.estoque)
      }),
    })
    .then(async res => {
      if (!res.ok) {
        const erro = await res.json().catch(() => ({}));
        throw new Error(erro.mensagem || 'Erro ao salvar jogo');
      }
      return res.json().catch(() => ({}));
    })
        .then(() => {
          mostrarAlertaSucesso(editandoJogoId ? '✏️ Jogo atualizado com sucesso!' : '✨ Jogo adicionado com sucesso!');
          limparFormJogo();
          carregarDados();
        })
        .catch(err => alert(err.message));
  };

  const editarJogo = (j) => {
    setEditandoJogoId(j.id);
    setNovoJogo({ titulo: j.titulo, plataforma: j.plataforma, preco: j.preco, estoque: j.estoque });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const limparFormJogo = () => {
    setEditandoJogoId(null);
    setNovoJogo({ titulo: '', plataforma: '', preco: '', estoque: '' });
  };
  const cadastrarCliente = (e) => {
    e.preventDefault();
    fetch(API_CLIENTES, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(novoCliente),
    })
    .then(async res => {
      if (!res.ok) {
        const erro = await res.json().catch(() => ({}));
        throw new Error(erro.mensagem || 'Erro ao registar cliente');
      }
      return res.json().catch(() => ({}));
    })
    .then(() => { 
      setNovoCliente({ nome: '', email: '' }); 
      carregarDados(); 
      mostrarAlertaSucesso('✨ Cliente registado com sucesso!');
    })
    .catch(err => alert(err.message));
  };

  const cadastrarPedido = (e) => {
    e.preventDefault();
    fetch(API_PEDIDOS, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        clienteId: Number(novoPedido.clienteId),
        jogoId: Number(novoPedido.jogoId),
        quantidade: Number(novoPedido.quantidade)
      }),
    })
    .then(async res => {
      if (!res.ok) {
        const erro = await res.json().catch(() => ({}));
        throw new Error(erro.mensagem || 'Erro ao criar pedido');
      }
      return res.json().catch(() => ({}));
    })
    .then(() => { 
      setNovoPedido({ clienteId: '', jogoId: '', quantidade: '' }); 
      carregarDados(); 
      mostrarAlertaSucesso('✨ Pedido efetuado com sucesso!');
    })
    .catch(err => alert(err.message));
  };

  const cancelarPedido = (id) => {
    fetch(`${API_PEDIDOS}/${id}/cancelamento`, { method: 'PATCH' })
        .then(async res => {
          if (!res.ok) {
            const erro = await res.json().catch(() => ({}));
            throw new Error(erro.mensagem || 'Erro ao cancelar pedido');
          }
          carregarDados();
          mostrarAlertaSucesso('🚫 Pedido cancelado. Estoque devolvido.');
        })
        .catch(err => alert(err.message));  };

  // Filtragem dos jogos por título, plataforma e preço máximo
  const jogosFiltrados = jogos.filter(j => {
    const matchTitulo = j.titulo.toLowerCase().includes(termoBusca.toLowerCase());
    const matchPlataforma = filtroPlataforma === '' || j.plataforma.toLowerCase().includes(filtroPlataforma.toLowerCase());
    const matchPreco = precoMaximo === '' || Number(j.preco) <= Number(precoMaximo);
    return matchTitulo && matchPlataforma && matchPreco;
  });

  return (
    <div style={styles.container}>
      {mensagemSucesso && (
        <div style={styles.toast}>
          {mensagemSucesso}
        </div>
      )}

      <header style={styles.header}>
        <h1 style={styles.title}>Nexus Games</h1>
        <p style={styles.subtitle}>Gestão Integrada de Loja de Jogos</p>
      </header>

      <div style={styles.navTabs}>
        {[
          { id: 'jogos', label: '🎮 Jogos', count: jogos.length },
          { id: 'clientes', label: '👥 Clientes', count: clientes.length },
          { id: 'pedidos', label: '📦 Pedidos', count: pedidos.length }
        ].map((item) => (
          <button
            key={item.id}
            onClick={() => setAba(item.id)}
            style={{
              ...styles.tabButton,
              ...(aba === item.id ? styles.tabActive : {})
            }}
          >
            {item.label}
            <span style={{
              ...styles.tabCounter,
              ...(aba === item.id ? styles.counterActive : {})
            }}>
              {item.count}
            </span>
          </button>
        ))}
      </div>

      <main style={styles.card}>
        {aba === 'jogos' && (
          <div>
            <h2 style={styles.sectionTitle}>Biblioteca de Jogos</h2>
            
            <form onSubmit={salvarJogo} style={styles.formGrid}>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Título do Jogo</label>
                <input style={styles.input} type="text" placeholder="Ex: Cyberpunk 2077" value={novoJogo.titulo} onChange={e => setNovoJogo({...novoJogo, titulo: e.target.value})} required />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Plataforma</label>
                <input style={styles.input} type="text" placeholder="Ex: PC / PS5" value={novoJogo.plataforma} onChange={e => setNovoJogo({...novoJogo, plataforma: e.target.value})} required />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Preço (R$)</label>
                <input style={styles.input} type="number" step="any" placeholder="0.00" value={novoJogo.preco} onChange={e => setNovoJogo({...novoJogo, preco: e.target.value})} required />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Estoque</label>
                <input style={styles.input} type="number" placeholder="0" value={novoJogo.estoque} onChange={e => setNovoJogo({...novoJogo, estoque: e.target.value})} required />
              </div>
              <div style={styles.fullWidth}>
                <button style={styles.submitButton} type="submit">
                  {editandoJogoId ? '💾 Salvar Alterações' : '+ Adicionar Jogo à Loja'}
                </button>
                {editandoJogoId && (
                    <button type="button" onClick={limparFormJogo} style={{ ...styles.cancelButton, width: '100%', marginTop: '10px' }}>
                      Cancelar edição
                    </button>
                )}              </div>
            </form>

            <div style={styles.filterContainer}>
              <input 
                style={styles.searchInput} 
                type="text" 
                placeholder="🔍 Pesquisar por título..." 
                value={termoBusca} 
                onChange={e => setTermoBusca(e.target.value)} 
              />
              <input 
                style={styles.searchInput} 
                type="text" 
                placeholder="🎮 Filtrar por plataforma..." 
                value={filtroPlataforma} 
                onChange={e => setFiltroPlataforma(e.target.value)} 
              />
              <input 
                style={styles.searchInput} 
                type="number" 
                step="any"
                placeholder="💰 Preço máximo (R$)..." 
                value={precoMaximo} 
                onChange={e => setPrecoMaximo(e.target.value)} 
              />
            </div>

            <div style={styles.gridList}>
              {jogosFiltrados.length === 0 ? (
                <p style={styles.emptyText}>Nenhum jogo encontrado com esses critérios.</p>
              ) : (
                jogosFiltrados.map(j => {
                  const estoqueBaixo = Number(j.estoque) < 3;
                  return (
                      <div key={j.id} style={styles.listItem}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                        <div style={styles.coverContainer}>
                          <span style={{ fontSize: '22px' }}>🎮</span>
                        </div>
                        <div>
                          <strong style={{ color: '#fff', fontSize: '17px' }}>{j.titulo}</strong>
                          <p style={{ color: '#94a3b8', margin: '6px 0 0', fontSize: '14px', display: 'flex', gap: '10px', alignItems: 'center', flexWrap: 'wrap' }}>
                            <span>Plataforma: <strong style={{ color: '#a5b4fc' }}>{j.plataforma}</strong></span>
                            <span>•</span>
                            <span style={estoqueBaixo ? styles.stockBaixoBadge : styles.stockNormalBadge}>
                              Stock: {j.estoque} un.
                            </span>
                          </p>
                        </div>
                      </div>
                      <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                        <span style={styles.badge}>R$ {Number(j.preco || 0).toFixed(2)}</span>
                        <button onClick={() => editarJogo(j)} style={styles.editButton}>Editar</button>
                      </div>
                    </div>
                  );
                })
              )}
            </div>
          </div>
        )}

        {aba === 'clientes' && (
          <div>
            <h2 style={styles.sectionTitle}>Registo de Clientes</h2>
            <form onSubmit={cadastrarCliente} style={styles.formGrid}>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Nome Completo</label>
                <input style={styles.input} type="text" placeholder="Ex: Ketely Hornns" value={novoCliente.nome} onChange={e => setNovoCliente({...novoCliente, nome: e.target.value})} required />
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>E-mail</label>
                <input style={styles.input} type="email" placeholder="exemplo@email.com" value={novoCliente.email} onChange={e => setNovoCliente({...novoCliente, email: e.target.value})} required />
              </div>
              <div style={styles.fullWidth}>
                <button style={styles.submitButton} type="submit">+ Registar Novo Cliente</button>
              </div>
            </form>

            <div style={styles.gridList}>
              {clientes.length === 0 ? (
                <p style={styles.emptyText}>Nenhum cliente registado no momento.</p>
              ) : (
                clientes.map(c => (
                    <div key={c.id} style={styles.listItem}>
                    <div>
                      <strong style={{ color: '#fff', fontSize: '17px' }}>{c.nome}</strong>
                      <p style={{ color: '#94a3b8', margin: '4px 0 0', fontSize: '14px' }}>{c.email}</p>
                    </div>
                    <span style={styles.idBadge}>ID #{c.id}</span>
                  </div>
                ))
              )}
            </div>
          </div>
        )}

        {aba === 'pedidos' && (
          <div>
            <h2 style={styles.sectionTitle}>Gestão de Pedidos</h2>
            <form onSubmit={cadastrarPedido} style={styles.formGrid}>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Cliente</label>
                <select style={styles.input} value={novoPedido.clienteId} onChange={e => setNovoPedido({...novoPedido, clienteId: e.target.value})} required>
                  <option value="">Selecione o cliente...</option>
                  {clientes.map(c => (
                      <option key={c.id} value={c.id}>{c.nome} ({c.email})</option>
                  ))}
                </select>              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Jogo</label>
                <select style={styles.input} value={novoPedido.jogoId} onChange={e => setNovoPedido({...novoPedido, jogoId: e.target.value})} required>
                  <option value="">Selecione o jogo...</option>
                  {jogos.map(j => (
                      <option key={j.id} value={j.id}>{j.titulo} ({j.plataforma}) | estoque: {j.estoque}</option>                  ))}
                </select>
              </div>
              <div style={styles.inputGroup}>
                <label style={styles.label}>Quantidade</label>
                <input style={styles.input} type="number" placeholder="1" value={novoPedido.quantidade} onChange={e => setNovoPedido({...novoPedido, quantidade: e.target.value})} required />
              </div>
              <div style={styles.fullWidth}>
                <button style={styles.submitButton} type="submit">+ Efetuar Pedido</button>
              </div>
            </form>

            <div style={styles.gridList}>
              {pedidos.length === 0 ? (
                <p style={styles.emptyText}>Nenhum pedido efetuado no momento.</p>
              ) : (
                  pedidos.map(p => (
                      <div key={p.id} style={styles.listItem}>
                        <div>
                          <strong style={{ color: '#fff', fontSize: '17px' }}>Pedido #{p.id}</strong>
                          <p style={{ color: '#94a3b8', margin: '4px 0 0', fontSize: '14px' }}>
                            {p.cliente} | {p.jogo} | Qtd: {p.quantidade} | Total: R$ {Number(p.valorTotal || 0).toFixed(2)}
                          </p>
                        </div>
                    <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                      <span style={p.status === 'CONFIRMADO' ? styles.badgeSuccess : styles.badgeCancelado}>
                        {p.status === 'CONFIRMADO' ? 'Confirmado' : 'Cancelado'}
                      </span>
                      {p.status === 'CONFIRMADO' && (
                          <button onClick={() => cancelarPedido(p.id)} style={styles.cancelButton}>Cancelar</button>
                      )}
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        )}
      </main>
    </div>
  );
}

const styles = {
  container: {
    fontFamily: "'Inter', system-ui, -apple-system, sans-serif",
    minHeight: '100vh',
    backgroundColor: '#090d16',
    color: '#f8fafc',
    padding: '50px 20px',
    position: 'relative',
  },
  toast: {
    position: 'fixed',
    top: '20px',
    right: '20px',
    backgroundColor: '#10b981',
    color: '#fff',
    padding: '12px 24px',
    borderRadius: '12px',
    fontWeight: '600',
    boxShadow: '0 10px 25px rgba(16, 185, 129, 0.4)',
    zIndex: 1000,
  },
  header: {
    textAlign: 'center',
    marginBottom: '36px',
  },
  title: {
    fontSize: '3rem',
    fontWeight: '800',
    background: 'linear-gradient(135deg, #818cf8 0%, #c084fc 100%)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
    marginBottom: '6px',
    lineHeight: '1.3',
    paddingBottom: '4px',
  },
  subtitle: {
    color: '#94a3b8',
    fontSize: '1.05rem',
  },
  navTabs: {
    display: 'flex',
    justifyContent: 'center',
    gap: '14px',
    marginBottom: '40px',
  },
  tabButton: {
    backgroundColor: '#111827',
    color: '#94a3b8',
    border: '1px solid #1f2937',
    padding: '12px 24px',
    borderRadius: '14px',
    cursor: 'pointer',
    fontWeight: '600',
    fontSize: '1rem',
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
  },
  tabActive: {
    backgroundColor: '#6366f1',
    color: '#ffffff',
    borderColor: '#818cf8',
    boxShadow: '0 0 25px rgba(99, 102, 241, 0.4)',
    transform: 'translateY(-2px)',
  },
  tabCounter: {
    backgroundColor: '#1f2937',
    color: '#cbd5e1',
    padding: '2px 8px',
    borderRadius: '10px',
    fontSize: '0.8rem',
    fontWeight: '700',
  },
  counterActive: {
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
    color: '#ffffff',
  },
  card: {
    maxWidth: '900px',
    margin: '0 auto',
    backgroundColor: '#111827',
    border: '1px solid #1f2937',
    borderRadius: '24px',
    padding: '40px',
    boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)',
  },
  sectionTitle: {
    fontSize: '1.6rem',
    fontWeight: '700',
    marginBottom: '24px',
    color: '#f8fafc',
    borderBottom: '1px solid #1f2937',
    paddingBottom: '14px',
  },
  formGrid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(2, minmax(0, 1fr))',
    gap: '20px',
    marginBottom: '24px',
  },
  filterContainer: {
    display: 'grid',
    gridTemplateColumns: 'repeat(3, 1fr)',
    gap: '15px',
    marginBottom: '24px',
  },
  searchInput: {
    backgroundColor: '#030712',
    border: '1px solid #374151',
    borderRadius: '12px',
    padding: '12px 16px',
    color: '#fff',
    fontSize: '0.95rem',
    outline: 'none',
  },
  inputGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
  },
  label: {
    fontSize: '0.85rem',
    fontWeight: '600',
    color: '#94a3b8',
  },
  input: {
    backgroundColor: '#030712',
    border: '1px solid #374151',
    borderRadius: '12px',
    padding: '14px 18px',
    color: '#fff',
    fontSize: '1rem',
    outline: 'none',
  },
  fullWidth: {
    gridColumn: 'span 2',
  },
  submitButton: {
    width: '100%',
    backgroundColor: '#10b981',
    color: '#ffffff',
    border: 'none',
    borderRadius: '12px',
    padding: '16px',
    fontWeight: '700',
    fontSize: '1rem',
    cursor: 'pointer',
    boxShadow: '0 4px 14px rgba(16, 185, 129, 0.3)',
  },
  cancelButton: {
    backgroundColor: 'rgba(239, 68, 68, 0.15)',
    color: '#f87171',
    border: '1px solid rgba(239, 68, 68, 0.3)',
    padding: '8px 16px',
    borderRadius: '10px',
    cursor: 'pointer',
    fontWeight: '600',
    fontSize: '0.85rem',
  },
  gridList: {
    display: 'flex',
    flexDirection: 'column',
    gap: '14px',
  },
  listItem: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#030712',
    border: '1px solid #1f2937',
    padding: '16px 20px',
    borderRadius: '16px',
  },
  coverContainer: {
    width: '45px',
    height: '45px',
    borderRadius: '10px',
    backgroundColor: '#1f2937',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexShrink: '0',
    border: '1px solid #374151',
  },
  emptyText: {
    textAlign: 'center',
    color: '#64748b',
    padding: '20px 0',
    fontStyle: 'italic',
  },
  badge: {
    backgroundColor: 'rgba(99, 102, 241, 0.15)',
    color: '#818cf8',
    padding: '8px 16px',
    borderRadius: '20px',
    fontWeight: '700',
    fontSize: '0.95rem',
    border: '1px solid rgba(99, 102, 241, 0.3)',
    flexShrink: '0',
  },
  stockNormalBadge: {
    color: '#34d399',
    backgroundColor: 'rgba(52, 211, 153, 0.1)',
    padding: '2px 8px',
    borderRadius: '6px',
    fontWeight: '600',
    fontSize: '0.8rem',
  },
  stockBaixoBadge: {
    color: '#f87171',
    backgroundColor: 'rgba(248, 113, 113, 0.15)',
    padding: '2px 8px',
    borderRadius: '6px',
    fontWeight: '700',
    fontSize: '0.8rem',
    border: '1px solid rgba(248, 113, 113, 0.3)',
  },
  idBadge: {
    backgroundColor: 'rgba(234, 179, 8, 0.15)',
    color: '#facc15',
    padding: '8px 16px',
    borderRadius: '20px',
    fontWeight: '600',
    fontSize: '0.9rem',
    border: '1px solid rgba(234, 179, 8, 0.3)',
  },
  badgeSuccess: {
    backgroundColor: 'rgba(16, 185, 129, 0.15)',
    color: '#34d399',
    padding: '8px 16px',
    borderRadius: '20px',
    fontWeight: '600',
    fontSize: '0.9rem',
    border: '1px solid rgba(16, 185, 129, 0.3)',
  },
    badgeCancelado: {
      backgroundColor: 'rgba(239, 68, 68, 0.15)',
      color: '#f87171',
      padding: '8px 16px',
      borderRadius: '20px',
      fontWeight: '600',
      fontSize: '0.9rem',
      border: '1px solid rgba(239, 68, 68, 0.3)',
    },
    editButton: {
      backgroundColor: 'rgba(99, 102, 241, 0.15)',
      color: '#a5b4fc',
      border: '1px solid rgba(99, 102, 241, 0.3)',
      padding: '8px 16px',
      borderRadius: '10px',
      cursor: 'pointer',
      fontWeight: '600',
      fontSize: '0.85rem',
    }
};