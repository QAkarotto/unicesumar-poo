import { useState } from 'react'
import { useBanco } from '../../services/store'
import { api } from '../../services/api'
import { useToast } from '../../components/Toast'
import { Codigos } from '../../components/Codigos'
import { StatusPill } from '../../components/StatusPill'
import {
  cultura,
  maisRecentes,
  nomeDoTecnico,
  notasDoTecnico,
  propriedade,
  usuario,
} from '../../data/consultas'
import { ADMIN_ID } from '../../data/constantes'
import { STATUS_QUE_ACEITAM_ATRIBUICAO, verificarAtribuicao } from '../../domain/regras'
import { ETAPAS, type Perfil, type Status } from '../../domain/tipos'
import { formatarData, formatarDataHora, formatarNota, media } from '../../utils/formato'
import { mensagemDeErro } from '../../utils/erros'
import { FormTecnico } from './FormTecnico'

const NOME_PERFIL: Record<Perfil, string> = {
  produtor: 'Produtor',
  tecnico: 'Técnico',
  admin: 'Administrador',
}

export function AdminPage() {
  const banco = useBanco()
  const toast = useToast()
  const [filtro, setFiltro] = useState<Status | ''>('')

  const contagem = ETAPAS.map((status) => ({
    status,
    total: banco.solicitacoes.filter((s) => s.status === status).length,
  }))
  const notaGeral = media(banco.avaliacoes.map((a) => a.nota))
  const lista = maisRecentes(banco.solicitacoes.filter((s) => !filtro || s.status === filtro))

  async function atribuir(solicitacaoId: string, tecnicoId: string) {
    if (!tecnicoId) return
    try {
      const nome = await api.atribuirTecnico(ADMIN_ID, solicitacaoId, tecnicoId)
      const s = banco.solicitacoes.find((x) => x.id === solicitacaoId)
      toast(`${s?.protocolo} atribuída a ${nome}`)
    } catch (err) {
      toast(mensagemDeErro(err))
    }
  }

  async function alterarSituacao(tecnicoId: string, ativo: boolean) {
    try {
      await api.alterarSituacaoTecnico(tecnicoId, ativo)
      const t = banco.tecnicos.find((x) => x.id === tecnicoId)
      const nome = t ? usuario(banco, t.usuarioId).nome : ''
      toast(
        ativo ? `${nome}: registro ativo` : `${nome}: registro inativo, não pode receber novas solicitações`,
      )
    } catch (err) {
      toast(mensagemDeErro(err))
    }
  }

  return (
    <div className="stack-lg">
      <div className="notebar">
        <span className="eyebrow">Nesta tela</span>
        <p>
          A coordenação acompanha todas as demandas e atribui o técnico. Na lista só ficam liberados técnicos
          com registro ativo e habilitados na cultura. Experimente atribuir a AGA-2026-0142 (feijão): só
          Daniel pode atender.
        </p>
        <Codigos rf={['RF01', 'RF04', 'RF05']} rn={['RN02', 'RN05']} />
      </div>

      <div className="kpis">
        {contagem.map(({ status, total }) => (
          <div className="kpi" key={status}>
            <span className="n">{total}</span>
            <StatusPill status={status} />
          </div>
        ))}
        <div className="kpi">
          <span className="n">{formatarNota(notaGeral)}</span>
          <span className="l">Nota média dos atendimentos</span>
        </div>
      </div>

      <div className="panel">
        <div className="panel-h">
          <h2>Solicitações</h2>
          <div className="filtro">
            <label htmlFor="a-filtro">Status</label>
            <select id="a-filtro" value={filtro} onChange={(e) => setFiltro(e.target.value as Status | '')}>
              <option value="">Todos</option>
              {ETAPAS.map((st) => (
                <option key={st}>{st}</option>
              ))}
            </select>
          </div>
        </div>
        <div className="tbl-wrap">
          <table>
            <thead>
              <tr>
                <th>Protocolo</th>
                <th>Aberta em</th>
                <th>Produtor</th>
                <th>Cultura</th>
                <th>Município</th>
                <th>Status</th>
                <th>Técnico responsável</th>
              </tr>
            </thead>
            <tbody>
              {lista.length ? (
                lista.map((s) => {
                  const nomeCultura = cultura(banco, s.culturaId).nome
                  return (
                    <tr key={s.id}>
                      <td className="mono">{s.protocolo}</td>
                      <td className="num-c">{formatarData(s.criadaEm)}</td>
                      <td>{usuario(banco, s.produtorId).nome}</td>
                      <td>{nomeCultura}</td>
                      <td>{propriedade(banco, s.propriedadeId).municipio}</td>
                      <td>
                        <StatusPill status={s.status} />
                      </td>
                      <td>
                        {STATUS_QUE_ACEITAM_ATRIBUICAO.includes(s.status) ? (
                          <>
                            <label className="sr" htmlFor={`as-${s.id}`}>
                              Técnico para {s.protocolo}
                            </label>
                            <select
                              id={`as-${s.id}`}
                              className="assign"
                              value={s.tecnicoId ?? ''}
                              onChange={(e) => atribuir(s.id, e.target.value)}
                            >
                              {!s.tecnicoId && <option value="">Selecionar técnico</option>}
                              {banco.tecnicos.map((t) => {
                                const r = verificarAtribuicao(t, s, nomeCultura)
                                return (
                                  <option key={t.id} value={t.id} disabled={!r.ok}>
                                    {usuario(banco, t.usuarioId).nome}
                                    {r.ok ? '' : ` (${r.motivo})`}
                                  </option>
                                )
                              })}
                            </select>
                          </>
                        ) : (
                          nomeDoTecnico(banco, s.tecnicoId)
                        )}
                      </td>
                    </tr>
                  )
                })
              ) : (
                <tr>
                  <td colSpan={7} className="muted">
                    Nenhuma solicitação com esse status.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <div className="admin-cols">
        <div className="stack-lg">
          <div className="panel">
            <div className="panel-h">
              <h2>Técnicos</h2>
              <span className="muted">{banco.tecnicos.length} cadastrados</span>
            </div>
            <div className="tbl-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Registro</th>
                    <th>Habilitado em</th>
                    <th>Atend.</th>
                    <th>Nota</th>
                    <th>Situação</th>
                  </tr>
                </thead>
                <tbody>
                  {banco.tecnicos.map((t) => (
                    <tr key={t.id}>
                      <td>
                        <b>{usuario(banco, t.usuarioId).nome}</b>
                        <br />
                        <span className="muted pequeno">{t.formacao}</span>
                      </td>
                      <td className="mono">{t.registro}</td>
                      <td>
                        <span className="chips">
                          {t.culturaIds.map((c) => (
                            <span className="chip" key={c}>
                              {cultura(banco, c).nome}
                            </span>
                          ))}
                        </span>
                      </td>
                      <td className="num-c">
                        {banco.atendimentos.filter((a) => a.tecnicoId === t.id).length}
                      </td>
                      <td className="num-c">{formatarNota(media(notasDoTecnico(banco, t.id)))}</td>
                      <td>
                        <label className="toggle">
                          <input
                            type="checkbox"
                            checked={t.ativo}
                            onChange={(e) => alterarSituacao(t.id, e.target.checked)}
                          />
                          {t.ativo ? 'Ativo' : 'Inativo'}
                        </label>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="panel">
            <div className="panel-h">
              <h2>Usuários</h2>
              <span className="muted">{banco.usuarios.length}</span>
            </div>
            <div className="tbl-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Perfil</th>
                    <th>Município</th>
                    <th>Consentimento LGPD</th>
                  </tr>
                </thead>
                <tbody>
                  {banco.usuarios.map((u) => (
                    <tr key={u.id}>
                      <td>{u.nome}</td>
                      <td>{NOME_PERFIL[u.perfil]}</td>
                      <td>{u.municipio}</td>
                      <td className="num-c">{formatarDataHora(u.consentimentoEm)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <FormTecnico />
      </div>
    </div>
  )
}
