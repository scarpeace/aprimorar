# Relatorio e eventos do aluno

## Objetivo

Unificar a experiencia da pagina de detalhes do aluno:

- eventos vinculados continuam sendo a lista operacional
- resumo financeiro fica acima da tabela, no mesmo layout do gerador de relatorios
- relatorio final sera gerado depois a partir do periodo selecionado

## Periodo inicial

Ao abrir a tela, usar o mes atual:

- `dataInicio`: primeiro dia do mes atual
- `dataFim`: ultimo dia do mes atual

No client, isso pode ser feito com `date-fns`:

```ts
format(startOfMonth(new Date()), "yyyy-MM-dd");
format(endOfMonth(new Date()), "yyyy-MM-dd");
```

## Regras da tabela

A tabela de eventos vinculados deve:

- mostrar todos os atendimentos do aluno no periodo, inclusive cancelados
- continuar paginada
- continuar com busca, status e tipo
- ordenar sempre por `dataHoraInicio,desc` e depois `id,asc`
- manter toggle de pagamento do aluno
- usar `inicio` e `fim` no `useGetAtendimentos`, sem `anoMes`

## Regras dos cards

Os cards acima da tabela devem:

- obedecer somente ao periodo selecionado
- nao acompanhar busca, status ou tipo
- mostrar:
  - atendimentos
  - total
  - pago
  - pendente

Por enquanto, usar `useGetRelatorioAluno` para preencher os cards.

## Mudancas no client

1. Adaptar `AlunoAtendimentos` para receber apenas `alunoId`. OK
2. Mover os inputs de periodo para dentro de `AlunoAtendimentos`. OK
3. Trocar filtro mensal por `inicio` e `fim`. OK
4. Renderizar os cards dentro de `AlunoAtendimentos`. OK
5. Remover de `AlunoDetails`: OK
   - `MonthYearSelector`
   - `AlunoFinanceSummary`
   - `AlunoRelatorio`
   - estados de ano/mes

## Depois

- adicionar endpoint PDF. OK
- implementar conteudo do PDF com OpenPDF. OK
- adicionar botao `Gerar relatorio`
- decidir se abre outra pagina, modal ou preview dedicado
- decidir envio por email/WhatsApp
