export async function request(path, { method = 'GET', body } = {}) {
  let response;
  try {
    response = await fetch(`/api${path}`, {
      method,
      headers: body === undefined ? {} : { 'Content-Type': 'application/json' },
      body: body === undefined ? undefined : JSON.stringify(body),
      signal: AbortSignal.timeout(15000),
    });
  } catch {
    throw new Error('Não foi possível conectar à biblioteca. Verifique se o backend está em execução e tente novamente.');
  }
  const data = await response.json().catch(() => null);
  if (!response.ok) {
    const fields = data?.campos ? Object.entries(data.campos).map(([field, message]) => `${field}: ${message}`).join(' · ') : '';
    throw new Error([data?.mensagem || `Falha na requisição (${response.status}).`, fields].filter(Boolean).join(' '));
  }
  if (data === null) throw new Error('A API retornou uma resposta inválida. Verifique o backend.');
  return data;
}
