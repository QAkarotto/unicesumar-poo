/**
 * Exibe o resultado de uma operação: sucesso (texto) ou erro (ApiError),
 * incluindo a lista de campos inválidos quando a API responde 400.
 */
export default function Mensagem({ sucesso, erro, aoFechar }) {
  if (!sucesso && !erro) return null

  return (
    <div className={`mensagem ${erro ? 'mensagem-erro' : 'mensagem-sucesso'}`} role="alert">
      <div>
        {erro && erro.status > 0 && <strong>HTTP {erro.status}: </strong>}
        {erro ? erro.message : sucesso}
        {erro?.campos && (
          <ul>
            {Object.entries(erro.campos).map(([campo, msg]) => (
              <li key={campo}>
                <code>{campo}</code>: {msg}
              </li>
            ))}
          </ul>
        )}
      </div>
      {aoFechar && (
        <button type="button" className="botao-fechar" onClick={aoFechar} aria-label="Fechar">
          ×
        </button>
      )}
    </div>
  )
}
