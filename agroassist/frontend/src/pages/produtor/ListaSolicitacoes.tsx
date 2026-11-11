import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { useProdutorId } from '../../context/Sessao'
import { cultura, maisRecentes, propriedade } from '../../data/consultas'
import { podeAvaliar } from '../../domain/regras'
import { StatusPill } from '../../components/StatusPill'
import { formatarDataHora } from '../../utils/formato'

export function ListaSolicitacoes() {
  const banco = useBanco()
  const produtorId = useProdutorId()
  const minhas = maisRecentes(banco.solicitacoes.filter((s) => s.produtorId === produtorId))

  const emAberto = minhas.filter((s) => s.status === 'Aberta' || s.status === 'Atribuída').length
  const emAndamento = minhas.filter((s) => s.status === 'Em atendimento').length
  const finalizadas = minhas.filter((s) => s.status === 'Concluída' || s.status === 'Avaliada').length
  const [filtro, setFiltro] = useState('todas')
  const exibidas = minhas.filter(
    (s) =>
      filtro === 'todas' ||
      (filtro === 'abertas' && ['Aberta', 'Atribuída'].includes(s.status)) ||
      (filtro === 'andamento' && s.status === 'Em atendimento') ||
      (filtro === 'finalizadas' && ['Concluída', 'Avaliada'].includes(s.status)),
  )
  return (
    <div className="scr">
      <div className="scr-head">
        <h2>Minha lavoura</h2>
        <Link to="/produtor/nova" className="btn btn-primary btn-sm">
          Nova
        </Link>
      </div>
      <p className="panel-subtitle">Acompanhe seus atendimentos em um só lugar.</p>
      <div className="dashboard-stats" aria-label="Resumo das solicitações">
        <div>
          <strong>{emAberto}</strong>
          <span>Aguardando</span>
        </div>
        <div>
          <strong>{emAndamento}</strong>
          <span>Em andamento</span>
        </div>
        <div>
          <strong>{finalizadas}</strong>
          <span>Finalizadas</span>
        </div>
      </div>
      <Link className="dashboard-action" to="/produtor/nova">
        <span>
          <strong>Precisa de assistência?</strong>
          <small>Descreva o problema da sua lavoura.</small>
        </span>
        <span aria-hidden="true">＋</span>
      </Link>
      <div className="dashboard-filters">
        <label htmlFor="filtro-sol">Filtrar atendimentos</label>
        <select id="filtro-sol" value={filtro} onChange={(e) => setFiltro(e.target.value)}>
          <option value="todas">Todos</option>
          <option value="abertas">Aguardando</option>
          <option value="andamento">Em andamento</option>
          <option value="finalizadas">Finalizadas</option>
        </select>
      </div>
      {minhas.length ? (
        <div className="stack">
          {exibidas.map((s) => {
            const ultimo = s.historico[s.historico.length - 1]
            return (
              <Link key={s.id} to={`/produtor/solicitacoes/${s.id}`} className="sol-card">
                <span className="sol-top">
                  <span className="mono muted">{s.protocolo}</span>
                  <StatusPill status={s.status} />
                </span>
                <span className="sol-title">
                  {cultura(banco, s.culturaId).nome} · {propriedade(banco, s.propriedadeId).nome}
                </span>
                <span className="sol-meta">Atualizada em {formatarDataHora(ultimo.em)}</span>
                {podeAvaliar(banco, produtorId, s) && <span className="flag">Avalie o atendimento</span>}
              </Link>
            )
          })}
          {!exibidas.length && <p className="empty">Não há solicitações para este filtro.</p>}
        </div>
      ) : (
        <div className="empty">
          <p>
            Você ainda não abriu nenhuma solicitação. Quando abrir, ela aparece aqui com o status do
            atendimento.
          </p>
          <Link to="/produtor/nova" className="btn btn-primary">
            Abrir a primeira
          </Link>
        </div>
      )}
    </div>
  )
}
