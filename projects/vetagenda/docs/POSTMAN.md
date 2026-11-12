# Testando a API pelo Postman

Base URL: `http://localhost:8080`

## 1. Cadastrar especialidade
`POST /api/especialidades`
```json
{
  "nome": "Clínica geral",
  "descricao": "Consultas e avaliações gerais"
}
```

## 2. Cadastrar tutor
`POST /api/tutores`
```json
{
  "nome": "Maria Oliveira",
  "cpf": "12345678900",
  "telefone": "42999990000",
  "email": "maria@email.com"
}
```

## 3. Cadastrar animal
`POST /api/animais`
```json
{
  "nome": "Rex",
  "especie": "Cachorro",
  "raca": "SRD",
  "sexo": "Macho",
  "dataNascimento": "2022-05-10",
  "peso": 12.5,
  "tutorId": 1
}
```

## 4. Cadastrar veterinário
`POST /api/veterinarios`
```json
{
  "nome": "Dra. Ana Souza",
  "crmv": "CRMV-PR-12345",
  "telefone": "42988887777",
  "especialidadeId": 1
}
```

## 5. Criar agendamento
Use uma data futura no formato ISO:
`POST /api/agendamentos`
```json
{
  "dataHora": "2027-01-20T14:30:00",
  "motivo": "Consulta de rotina",
  "observacoes": "Aplicar reforço de vacina, se indicado",
  "animalId": 1,
  "veterinarioId": 1
}
```

## 6. Cancelar
`PATCH /api/agendamentos/1/cancelar`

## 7. Registrar atendimento
`POST /api/atendimentos`
```json
{
  "diagnostico": "Avaliação clínica realizada",
  "procedimento": "Exame físico",
  "observacoes": "Retorno conforme orientação veterinária",
  "agendamentoId": 1
}
```
