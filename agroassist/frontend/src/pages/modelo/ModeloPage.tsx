import { useState } from 'react'
import { useBanco } from '../../services/store'
import {
  ENTIDADES,
  EVOLUCOES,
  QUALIDADE,
  REGRAS,
  RELACIONAMENTOS,
  REQUISITOS,
  tabelaDaEntidade,
  type ChaveEntidade,
} from './conteudo'

export function ModeloPage() {
  const banco = useBanco()
  const [entidade, setEntidade] = useState<ChaveEntidade>('solicitacoes')
  const tabela = tabelaDaEntidade(banco, entidade)

  return (
    <div className="stack-xl">
      <section className="stack-md">
        <h2 className="sec-h">Estrutura inicial dos dados</h2>
        <p className="muted intro">
          As sete entidades do resumo. Atendimento e Avaliação ficam separados da Solicitação e ligados a ela,
          por isso o histórico de cada serviço continua consultável depois de concluído.
        </p>
        <div className="ent-grid">
          {ENTIDADES.map((e) => (
            <div className="ent" key={e.chave}>
              <h3>
                {e.nome}
                <span>{banco[e.chave].length} reg.</span>
              </h3>
              <ul>
                {e.atributos.map(([nome, detalhe]) => (
                  <li key={nome}>
                    <span className="k">{nome}</span>
                    <span className="t">{detalhe === 'PK' ? <span className="pk">PK</span> : detalhe}</span>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
        <div className="panel">
          <div className="panel-h">
            <h3>Relacionamentos</h3>
          </div>
          <div className="panel-b">
            <ul className="rels">
              {RELACIONAMENTOS.map(([cardinalidade, descricao]) => (
                <li key={descricao}>
                  <span className="card-n">{cardinalidade}</span>
                  <span>{descricao}</span>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </section>

      <section className="panel">
        <div className="panel-h">
          <h3>Registros atuais</h3>
          <span className="muted">atualiza conforme você usa o protótipo</span>
        </div>
        <div className="panel-b">
          <div className="seg" role="group" aria-label="Entidade">
            {ENTIDADES.map((e) => (
              <button
                type="button"
                key={e.chave}
                aria-pressed={entidade === e.chave}
                onClick={() => setEntidade(e.chave)}
              >
                {e.nome}
                <span className="c">{banco[e.chave].length}</span>
              </button>
            ))}
          </div>
          <div className="tbl-wrap">
            <table>
              <thead>
                <tr>
                  {tabela.colunas.map((c) => (
                    <th key={c}>{c}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {tabela.linhas.length ? (
                  tabela.linhas.map((linha) => (
                    <tr key={linha[0]}>
                      {linha.map((valor, i) => (
                        <td key={i} className={i === 0 ? 'mono' : undefined}>
                          {valor}
                        </td>
                      ))}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={tabela.colunas.length} className="muted">
                      Nenhum registro ainda.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </section>

      <div className="cols">
        <section className="panel">
          <div className="panel-h">
            <h3>Requisitos funcionais</h3>
            <span className="muted">MVP marcado</span>
          </div>
          <div className="panel-b">
            <ul className="reqs">
              {REQUISITOS.map((r) => (
                <li key={r.id}>
                  <span className="id">{r.id}</span>
                  <span>
                    {r.texto}
                    <br />
                    <span className="where-l">{r.onde}</span>
                  </span>
                  {r.mvp ? (
                    <span className="tag">MVP</span>
                  ) : (
                    <span className="tag out" title="Necessário para o fluxo, feito pela coordenação">
                      apoio
                    </span>
                  )}
                </li>
              ))}
            </ul>
          </div>
        </section>
        <section className="panel">
          <div className="panel-h">
            <h3>Regras de negócio</h3>
          </div>
          <div className="panel-b">
            <ul className="reqs">
              {REGRAS.map((r) => (
                <li key={r.id}>
                  <span className="id rn-id">{r.id}</span>
                  <span>{r.texto}</span>
                  <span className="where">{r.onde}</span>
                </li>
              ))}
            </ul>
          </div>
        </section>
      </div>

      <div className="cols">
        <section className="panel">
          <div className="panel-h">
            <h3>Qualidade, segurança e LGPD</h3>
            <span className="muted">ISO/IEC 25010</span>
          </div>
          <div className="panel-b">
            <ul className="q-list">
              {QUALIDADE.map(([titulo, texto]) => (
                <li key={titulo}>
                  <b>{titulo}</b>
                  {texto}
                </li>
              ))}
            </ul>
          </div>
        </section>
        <section className="panel">
          <div className="panel-h">
            <h3>Escopo do MVP</h3>
          </div>
          <div className="panel-b">
            <p className="texto">
              Entram no MVP: cadastro, abertura da solicitação, acompanhamento do status, registro do
              atendimento e avaliação.
            </p>
            <div>
              <span className="hint">Evoluções depois do MVP</span>
              <ul className="evolucoes">
                {EVOLUCOES.map((e) => (
                  <li key={e}>{e}</li>
                ))}
              </ul>
            </div>
          </div>
        </section>
      </div>
    </div>
  )
}
