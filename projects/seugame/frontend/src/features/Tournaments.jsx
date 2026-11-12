import { useState } from 'react'
import { useAuth } from '../auth/AuthContext.js'
import { Loading, PageError } from '../components/Feedback.jsx'
import TournamentEditorPage from '../pages/TournamentEditorPage.jsx'
import MyTournamentsPage from '../pages/MyTournamentsPage.jsx'
import TournamentPage from '../pages/TournamentPage.jsx'
import ScoresPage from '../pages/ScoresPage.jsx'
import useResource from '../hooks/useResource.js'
import useMutation from '../hooks/useMutation.js'
import { api, loadTournament } from '../services/api.js'
import { navigate } from '../lib/router.js'

export function CreateTournament() {
  const { notify } = useAuth()
  const mutation = useMutation()
  function submit(body) {
    mutation.run(async () => {
      const created = await api('/torneios', { method: 'POST', body })
      notify('Torneio criado e confrontos sorteados.')
      navigate('/torneios/' + created.torneio.id)
    })
  }
  return <TournamentEditorPage onSubmit={submit} busy={mutation.busy} error={mutation.error} />
}
export function MyTournaments() {
  const { notify } = useAuth()
  const resource = useResource('/usuarios/me/torneios')
  const mutation = useMutation()
  function remove(tournament) {
    if (!window.confirm('Excluir este torneio e todos os seus resultados? Esta ação não pode ser desfeita.')) return
    mutation.run(async () => {
      await api('/torneios/' + tournament.id, { method: 'DELETE' })
      notify('Torneio excluído.')
      resource.reload()
    })
  }
  return <MyTournamentsPage tournaments={resource.data || []} loading={resource.loading} error={mutation.error || resource.error}
    retry={resource.reload} onDelete={remove} busy={mutation.busy || resource.loading} />
}
export function Tournament({ id, page }) {
  const { user, notify } = useAuth()
  const resource = useResource(id, loadTournament)
  const mutation = useMutation()
  const [saved, setSaved] = useState(null)
  if (!resource.data) return resource.error ? <PageError error={resource.error} retry={resource.reload} /> : <Loading message="Carregando torneio…" />
  const data = resource.data
  const path = '/torneios/' + id
  const busy = mutation.busy || resource.loading
  if (page !== 'detail') {
    if (user?.id !== data.torneio.criador.id) return <PageError error={{ status: 403, message: 'Somente o criador pode gerenciar este torneio.' }} />
    if (data.torneio.status === 'FINALIZADO') return <TournamentPage data={data} user={user} />
  }
  function submit(body) {
    mutation.run(async () => {
      await api(path, { method: 'PUT', body })
      notify('Torneio atualizado.')
      navigate('/torneios/' + id)
    })
  }
  function finalize() {
    if (!window.confirm('Finalizar o torneio? Depois disso, placares e dados ficarão bloqueados para alterações.')) return
    mutation.run(async () => {
      await api(path + '/finalizacao', { method: 'POST' })
      notify('Torneio finalizado. Campeão definido!')
      resource.reload()
    })
  }
  function saveScore(matchId, body) {
    setSaved({ id: matchId, message: '' })
    mutation.run(async () => {
      await api(path + '/partidas/' + matchId + '/resultado', { method: 'PUT', body })
      setSaved({ id: matchId, message: 'Placar salvo. Competição atualizada.' })
      resource.reload()
    })
  }
  if (page === 'edit') return <TournamentEditorPage initial={data} matches={data.partidas} onSubmit={submit} busy={busy} error={mutation.error || resource.error} />
  if (page === 'scores') return <ScoresPage data={data} onSave={saveScore} busy={busy} error={mutation.error || resource.error} notice={saved?.message} feedbackId={saved?.id} />
  return <TournamentPage data={data} user={user} onFinalize={finalize} busy={busy} error={mutation.error || resource.error} />
}
