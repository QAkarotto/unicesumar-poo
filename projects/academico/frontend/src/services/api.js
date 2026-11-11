const BASE_URL = 'http://localhost:8080/api';

async function request(endpoint, options = {}) {
  const url = `${BASE_URL}${endpoint}`;
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };

  try {
    const response = await fetch(url, config);
    if (response.status === 204) {
      return null;
    }

    const contentType = response.headers.get('content-type');
    const isJson = contentType && contentType.includes('application/json');
    const data = isJson ? await response.json() : null;

    if (!response.ok) {
      const errorMessage = data?.mensagem || data?.message || `Erro ${response.status} na requisição.`;
      const error = new Error(errorMessage);
      error.status = response.status;
      error.data = data;
      throw error;
    }

    return data;
  } catch (error) {
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
      throw new Error('Falha de conexão com o servidor backend (porta 8080).');
    }
    throw error;
  }
}

export const api = {
  // Alunos
  listarAlunos: () => request('/alunos'),
  buscarAluno: (ra) => request(`/alunos/${ra}`),
  cadastrarAluno: (aluno) => request('/alunos', { method: 'POST', body: JSON.stringify(aluno) }),

  // Disciplinas
  listarDisciplinas: () => request('/disciplinas'),
  buscarDisciplina: (idOuCodigo) => request(`/disciplinas/${idOuCodigo}`),
  cadastrarDisciplina: (disciplina) => request('/disciplinas', { method: 'POST', body: JSON.stringify(disciplina) }),

  // Ofertas
  listarOfertas: () => request('/ofertas'),
  buscarOferta: (idOuCodigo) => request(`/ofertas/${idOuCodigo}`),
  cadastrarOferta: (oferta) => request('/ofertas', { method: 'POST', body: JSON.stringify(oferta) }),

  // Matrículas
  listarMatriculas: (ra) => request(ra ? `/matriculas?ra=${encodeURIComponent(ra)}` : '/matriculas'),
  buscarMatricula: (idOuCodigo) => request(`/matriculas/${idOuCodigo}`),
  matricular: (dados) => request('/matriculas', { method: 'POST', body: JSON.stringify(dados) }),
  concluirMatricula: (id, resultado) => request(`/matriculas/${id}/conclusao`, {
    method: 'PATCH',
    body: JSON.stringify({ resultado })
  }),
  trancarMatricula: (id) => request(`/matriculas/${id}/trancar`, { method: 'PATCH' }),
  cancelarMatricula: (id) => request(`/matriculas/${id}/cancelar`, { method: 'PATCH' }),
  excluirMatricula: (id) => request(`/matriculas/${id}`, { method: 'DELETE' }),
};
