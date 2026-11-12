import { useRef, useState } from 'react'
import Icon from './Icon.jsx'
import Link from './Link.jsx'
import { Message } from './Feedback.jsx'
import { today } from '../lib/format.js'

export default function TournamentForm({ initial, editing = false, formatLocked = false, participantsLocked = false, onSubmit, busy, error }) {
  const [participants, setParticipants] = useState(() => (initial?.participantes.map((p) => p.nome) || ['', '', '']).map((nome) => ({ id: crypto.randomUUID(), nome })))
  const [validation, setValidation] = useState('')
  const listRef = useRef(null)
  const cancel = editing ? `/torneios/${initial.torneio.id}` : '/meus-torneios'

  function addParticipant() {
    setParticipants((items) => [...items, { id: crypto.randomUUID(), nome: '' }])
    requestAnimationFrame(() => listRef.current?.lastElementChild?.querySelector('input').focus())
  }
  function removeParticipant(id) {
    if (participants.length <= 3) { setValidation('O torneio precisa ter pelo menos 3 participantes.'); return }
    setParticipants((items) => items.filter((p) => p.id !== id))
    setValidation('')
  }
  function submit(event) {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    const names = participants.map((p) => p.nome.trim())
    if (new Set(names.map((name) => name.toLocaleLowerCase('pt-BR'))).size !== names.length) {
      setValidation('Use nomes diferentes para cada participante.'); return
    }
    setValidation('')
    onSubmit?.({ nome: data.get('nome').trim(), descricao: data.get('descricao').trim(),
      tipo: formatLocked ? initial.torneio.tipo : data.get('tipo'), dataInicio: data.get('dataInicio'), participantes: names })
  }
  return <>
    {formatLocked ? <Message type="info" message="O formato não pode ser alterado porque o torneio já foi iniciado." /> : null}
    {participantsLocked ? <Message type="info" message="Os participantes estão bloqueados para preservar os resultados registrados." /> : null}
    <Message message={validation || error?.message} errors={error?.fields} />
    <form className="form-card tournament-form" onSubmit={submit}>
      <fieldset disabled={busy} className="form-fieldset">
        <div className="form-section"><div className="form-section-title"><span>01</span><div><h2>Informações básicas</h2><p>Como a competição será apresentada.</p></div></div>
          <label>Nome do torneio<input name="nome" defaultValue={initial?.torneio.nome || ''} required minLength={3} maxLength={120} placeholder="Ex.: Copa de Inverno" /></label>
          <label>Descrição<textarea name="descricao" defaultValue={initial?.torneio.descricao || ''} rows={4} maxLength={2000} placeholder="Conte os detalhes, regras ou contexto da disputa." /></label>
          <div className="form-row"><label>Formato<select name="tipo" defaultValue={initial?.torneio.tipo || 'TABELA'} disabled={formatLocked} required>
            <option value="TABELA">Tabela — todos contra todos</option><option value="CHAVEAMENTO">Chaveamento — eliminatórias</option></select></label>
            <label>Data de início<input type="date" name="dataInicio" defaultValue={initial?.torneio.dataInicio || today()} required /></label></div>
        </div>
        <div className="form-section"><div className="form-section-title"><span>02</span><div><h2>Participantes</h2><p>{participantsLocked ? 'Lista preservada após o primeiro placar.' : editing ? 'Alterações apenas descritivas preservam o sorteio. Mudar a lista reconstrói os confrontos.' : 'Adicione no mínimo três. A ordem será sorteada.'}</p></div></div>
          <div className="participant-list" ref={listRef}>{participants.map((participant, index) => <div className="participant-input" key={participant.id}>
            <span className="participant-number">{index + 1}</span><input type="text" aria-label={`Participante ${index + 1}`} value={participant.nome} required maxLength={100} disabled={participantsLocked} placeholder="Nome do participante"
              onChange={(event) => setParticipants((items) => items.map((p) => p.id === participant.id ? { ...p, nome: event.target.value } : p))} />
            {!participantsLocked ? <button type="button" className="icon-button danger" aria-label={`Remover participante ${index + 1}`} onClick={() => removeParticipant(participant.id)}><Icon name="trash" /></button> : null}
          </div>)}</div>
          {!participantsLocked ? <button type="button" className="btn btn-ghost" onClick={addParticipant}><Icon name="plus" />Adicionar participante</button> : null}
        </div>
        <div className="form-actions"><Link className="btn btn-ghost" href={cancel}>Cancelar</Link><button className="btn btn-primary" type="submit"><Icon name={editing ? 'save' : 'shuffle'} />{busy ? 'Salvando…' : editing ? 'Salvar alterações' : 'Criar e sortear'}</button></div>
      </fieldset>
    </form>
  </>
}
