# Explicação

Entendi perfeitamente a dúvida! Vamos por partes.

## O problema

Tu percebeu que o filtro de data não é só da tabela, ele também vai afetar os KPIs. Se eu colocar `startDate` e `endDate` dentro de um hook da tabela, os KPIs não vão ter acesso a essa informação. Aí cada um lê de um lugar diferente e corre risco de ficarem dessincronizados.

## O que eu recomendo

Um hook de página, não de componente isolado. Ele fica no nível da `StudentDetailsPage` e centraliza todas as queries e todos os filtros que a página precisa. Os componentes filhos só recebem os dados prontos para renderizar.

A URL continua sendo a fonte da verdade dos filtros via `searchParams`, igual já é hoje. Mas em vez de cada componente ler `searchParams` por conta própria, o hook centraliza tudo e garante que KPIs e tabela usem os mesmos parâmetros.

## Como ficaria

```text
StudentDetailsPage
  └─ useStudentPageData(studentId)
       ├─ useStudentDateFilters()        → startDate, endDate
       ├─ lê hidePaid de searchParams
       ├─ useGetAppointmentsByStudentId  → eventsQuery
       └─ useGetStudentKPIs(studentId)   → kpisQuery
```

O hook devolve:

```ts
{
  eventsQuery,
  kpisQuery,
  startDate,
  endDate,
  handleStartDateChange,
  handleEndDateChange,
  handleClearFilters,
  hasFilters,
}
```

A página vira um orquestrador:

```tsx
export function StudentDetailsPage() {
  const { id } = useParams()
  const studentId = id ?? ""
  const [isFormOpen, setIsFormOpen] = useState(false)

  const {
    eventsQuery, kpisQuery,
    startDate, endDate, hasFilters,
    handleStartDateChange, handleEndDateChange, handleClearFilters,
  } = useStudentPageData(studentId)

  return (
    <PageLayout ...>
      <StudentInfoSection ... />
      <DateRangeInput ... />
      <StudentKPIs kpisQuery={kpisQuery} />
      <StudentEventsTable eventsQuery={eventsQuery} />
      <StudentForm modal />
    </PageLayout>
  )
}
```

E a `StudentEventsTable` perde toda a lógica de URL e query, recebe `eventsQuery` já resolvido como prop e só renderiza.

## E o `hideCharged`?

Mesma história: se os KPIs precisarem saber se está filtrando por pagos ou não pagos, entra no hook central. Se for só da tabela, fica local mesmo.

## Resumo

| O que você chamou | É isso |
|---|---|
| "hook único que controla todas as queries" | Sim, `useStudentPageData` |
| "controla tanto datas quanto hide paid" | Depende: se KPIs usarem, sim; senão, local na tabela |
| "retorna o resultado pra usar em tela" | Exato, componentes ficam mais focados em renderizar |

TL;DR: Um hook por página que centraliza filtros e queries, não um hook por componente.
