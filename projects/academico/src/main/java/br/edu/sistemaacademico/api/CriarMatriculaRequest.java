package br.edu.sistemaacademico.api;


  //JSON esperado no corpo de POST /api/matriculas:
  //{
    //"registroAcademico": "RA2026003","codigoTurma": "ESOFT4S-NB","codigoDisciplina": "BD"
  //}

public record CriarMatriculaRequest(
        String registroAcademico,
        String codigoTurma,
        String codigoDisciplina
) {

    public boolean temTodosOsCampos() {
        return !vazio(registroAcademico)
                && !vazio(codigoTurma)
                && !vazio(codigoDisciplina);
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
