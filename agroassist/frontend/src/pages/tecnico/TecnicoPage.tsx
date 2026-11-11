import { useSearchParams } from 'react-router-dom'
import { useBanco } from '../../services/store'
import { useSessao } from '../../context/Sessao'
import { Codigos } from '../../components/Codigos'
import { StatusPill } from '../../components/StatusPill'
import {
  cultura,
  maisRecentes,
  notasDoTecnico,
  propriedade,
  tecnicoDoUsuario,
  usuario,
} from '../../data/consultas'
import type { Solicitacao } from '../../domain/tipos'
import { formatarData, formatarNota, media } from '../../utils/formato'
import { DetalheTecnico } from './DetalheTecnico'

export function TecnicoPage() {
  const banco = useBanco()
  const { tecnicoUsuarioId, trocarTecnico } = useSessao()
  const [params, setParams] = useSearchParams()

  if (!banco.tecnicos.length) {
    return (
      <div className="panel">
        <div className="panel-b">
          <p>Nenhum técnico cadastrado. Cadastre um técnico na Coordenação.</p>
        </div>
      </div>
    )
  }

  const usuarioId = tecnicoDoUsuario(banco, tecnicoUsuarioId) ? tecnicoUsuarioId : banco.tecnicos[0].usuarioId
  const tec = tecnicoDoUsuario(banco, usuarioId)!
  const minhas = maisRecentes(banco.solicitacoes.filter((s) => s.tecnicoId === tec.id))
  const outras = maisRecentes(
    banco.solicitacoes.filter((s) => s.tecnicoId !== tec.id && s.status !== 'Avaliada'),
  )
  const selecionadaId = params.get('s') ?? minhas[0]?.id ?? outras[0]?.id ?? null
  const selecionada = banco.solicitacoes.find((s) => s.id === selecionadaId) ?? null

  function selecionar(id: string) {
    setParams({ s: id })
  }

  function trocar(id: string) {
    trocarTecnico(id)
    setParams({})
  }

  function tabela(lista: Solicitacao[], vazio: string) {
    if (!lista.length) return <p className="muted vazio">{vazio}</p>
    return (
      <div className="tbl-wrap">
        <table>
          <thead>
            <tr>
              <th>Protocolo</th>
              <th>Cultura</th>
              <th>Município</th>
              <th>Status</th>
              <th>Aberta em</th>
            </tr>
          </thead>
          <tbody>
            {lista.map((s) => (
              <tr key={s.id} className={s.id === selecionada?.id ? 'sel' : undefined}>
                <td>
                  <button type="button" className="linkbtn" onClick={() => selecionar(s.id)}>
                    {s.protocolo}
                  </button>
                </td>
                <td>{cultura(banco, s.culturaId).nome}</td>
                <td>{propriedade(banco, s.propriedadeId).municipio}</td>
                <td>
                  <StatusPill status={s.status} />
                </td>
                <td className="num-c">{formatarData(s.criadaEm)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )
  }

  return (
    <div className="stack-lg">
      <div className="notebar">
        <span className="eyebrow">Nesta tela</span>
        <p>
          O técnico vê as solicitações atribuídas a ele, inicia e registra o atendimento. As demais aparecem
          só para consulta, com o contato do produtor oculto. Experimente trocar o técnico no seletor.
        </p>
        <Codigos rf={['RF04', 'RF06']} rn={['RN03', 'RN05', 'RN06']} />
      </div>

      <div className="panel">
        <div className="panel-b">
          <div className="who-row">
            <div className="field">
              <label htmlFor="t-user">Entrou como</label>
              <select id="t-user" value={usuarioId} onChange={(e) => trocar(e.target.value)}>
                {banco.tecnicos.map((t) => (
                  <option key={t.id} value={t.usuarioId}>
                    {usuario(banco, t.usuarioId).nome}
                  </option>
                ))}
              </select>
            </div>
            <div className="prof">
              <span>
                <b>{tec.formacao}</b> · <span className="mono">{tec.registro}</span>
              </span>
              {tec.ativo ? (
                <span className="ok-tag">Registro ativo</span>
              ) : (
                <span className="bad-tag">Registro inativo</span>
              )}
              <span>Habilitado em:</span>
              <span className="chips">
                {tec.culturaIds.map((c) => (
                  <span className="chip" key={c}>
                    {cultura(banco, c).nome}
                  </span>
                ))}
              </span>
              <span>
                Nota média: <b>{formatarNota(media(notasDoTecnico(banco, tec.id)))}</b>
              </span>
            </div>
          </div>
        </div>
      </div>

      <div className="tec-grid">
        <div className="stack-lg">
          <div className="panel">
            <div className="panel-h">
              <h2>Minhas solicitações</h2>
              <span className="muted">{minhas.length}</span>
            </div>
            {tabela(minhas, 'Nenhuma solicitação atribuída a você no momento.')}
          </div>
          <div className="panel">
            <div className="panel-h">
              <h3>Outras solicitações da região</h3>
              <span className="muted">só consulta</span>
            </div>
            {tabela(outras, 'Nenhuma outra solicitação em aberto.')}
          </div>
        </div>
        {selecionada ? (
          <DetalheTecnico key={selecionada.id} solicitacao={selecionada} usuarioId={usuarioId} />
        ) : (
          <div className="panel">
            <div className="panel-b muted">Selecione uma solicitação para ver os detalhes.</div>
          </div>
        )}
      </div>
    </div>
  )
}
