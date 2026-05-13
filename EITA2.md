# EITA 2 - Replicar StudentDetailsPage em EmployeeDetailPage

## Estado atual

`StudentDetailsPage` e a referencia - a pagina centraliza tudo.
`EmployeeDetailPage` esta defasada: KPIs e stub, EventsTable e smart (fetches, paga, filtra).

## Gap detalhado

| Aspecto | Student | Employee |
|---------|---------|----------|
| Data fetching | Pagina: appointments + summary | EventsTable autogerenciado, KPIs null |
| Paginacao | `currentPage` na pagina, reset ao filtrar | `currentPage` dentro do EventsTable |
| Toggle | Premium na pagina + `setCurrentPage(0)` | Basico dentro do EventsTable |
| Date range | Conectado as queries | Nao conectado (EventsTable ignora) |
| KPIs | Presentacional (props) | Stub (null) |
| EventsTable | Presentacional (props) | Smart (fetch, pagina, filtra sozinho) |

## Etapas

### 1 - Rewritar `EmployeeKPIs` para presentacional

**Arquivo:** `client/src/features/employees/components/EmployeeKPIs.tsx`

Receber `totalEvents`, `totalPaid`, `totalUnpaid` como props.

Na pratica, seguir o mesmo padrao visual da tela de aluno com `KpiCard`:

```tsx
interface EmployeeKPIsProps {
  totalEvents?: number;
  totalPaid?: number;
  totalUnpaid?: number;
}

export function EmployeeKPIs({ totalEvents, totalPaid, totalUnpaid }: EmployeeKPIsProps) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
      <KpiCard label="Total de eventos" value={totalEvents} Icon={Calendar} />
      <KpiCard
        label="Total pago"
        value={<span className="text-success">{brl.format(totalPaid ?? 0)}</span>}
        Icon={CircleDollarSign}
        className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
      />
      <KpiCard
        label="Total pendente"
        value={<span className="text-warning">{brl.format(totalUnpaid ?? 0)}</span>}
        Icon={Clock3}
        className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
      />
    </div>
  );
}
```

### 2 - Rewritar `EmployeeEventsTable` para presentacional

**Arquivo:** `client/src/features/employees/components/EmployeeEventsTable.tsx`

Remover `useSearchParams`, `useState(currentPage)`, `useState(hidePaid)`, `useGetAppointmentsByEmployeeId`.
Manter so a UI da tabela + pagination + toggle payment button.

Seguir exatamente o padrao do `StudentEventsTable`: o toggle premium de filtro fica na pagina, e o table recebe apenas os dados necessarios para renderizar a listagem paginada. A mutation de marcar pagamento continua interna ao componente via `useAppointmentMutations()`.

```tsx
interface EmployeeEventsTableProps {
  appointments?: PageDTOAppointmentResponseDTO;
  currentPage: number;
  error?: unknown;
  isLoading: boolean;
  onPageChange: (page: number) => void;
}
```

O toggle `hidePaid` nao passa pelo table.
O `handleToggleEmployeePayment` nao vem como prop - ele continua interno ao componente, igual ao `StudentEventsTable` faz com `toggleStudentCharge`.

### 3 - Atualizar `EmployeeDetailPage`

**Arquivo:** `client/src/features/employees/pages/EmployeeDetailPage.tsx`

Adicionar no topo:

```tsx
const [currentPage, setCurrentPage] = useState(0);
const [hidePaid, setHidePaid] = useState(false);
```

Queries centralizadas:

```tsx
const employeeAppointments = useGetAppointmentsByEmployeeId(employeeId, {
  page: currentPage,
  sort: ["endDate,desc", "id,asc"],
  startDate: startDate?.toISOString(),
  endDate: endDate?.toISOString(),
  hidePaid: hidePaid ? false : undefined,
});

const employeeSummary = useGetEmployeeSummary(employeeId, {
  startDate: startDate?.toISOString(),
  endDate: endDate?.toISOString(),
});
```

Handlers com reset de pagina:

```tsx
const handleToggleHidePaid = () => {
  setHidePaid((curr) => !curr);
  setCurrentPage(0);
};

const handleStartDateFilterChange = (date: Date | null) => {
  setCurrentPage(0);
  handleStartDateChange(date);
};

const handleEndDateFilterChange = (date: Date | null) => {
  setCurrentPage(0);
  handleEndDateChange(date);
};
```

### 4 - Toggle premium visual para `hidePaid`

Seguir o mesmo encapsulamento do student mas com tema `info` (azul):

```tsx
<div className="shrink-0 rounded-2xl border border-info/20 bg-linear-to-r from-info/8 via-base-100 to-base-100 px-3 py-2 shadow-sm transition-all duration-200 hover:border-info/30 hover:shadow-md">
  <div className="flex items-center gap-3">
    <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-info/12 text-info">
      <CircleDollarSign className="w-4" />
    </div>
    <ToggleSwitch
      toggled={hidePaid}
      setToggle={handleToggleHidePaid}
      label={"Ocultar Pagos"}
      className="border-info/30 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
    />
  </div>
</div>
```

### 5 - Passar dados pros componentes filhos

```tsx
<EmployeeKPIs
  totalEvents={employeeSummary.data?.totalEvents}
  totalPaid={employeeSummary.data?.totalPaid}
  totalUnpaid={employeeSummary.data?.totalUnpaid}
/>

<EmployeeEventsTable
  appointments={employeeAppointments.data}
  currentPage={currentPage}
  error={employeeAppointments.error}
  isLoading={employeeAppointments.isLoading}
  onPageChange={setCurrentPage}
/>
```

## O que NAO muda

- Mutations (`toggleEmployeePayment`) - ja invalidam summary + list
- `use-appointment-mutations.ts` - intacto
- `useDateRangeFilters` - mesmo hook, ja importado
- Kubb hooks - `useGetAppointmentsByEmployeeId`, `useGetEmployeeSummary` ja existem
- Layout (`PageLayout`, `EmployeeEditModal`, `EmployeeInfoSection`) - intacto
- `EmployeeAppointmentMobileCard` - se existir, manter como esta

## Validacao

- `npm run lint` - sem erros
- `npm run build` - sem erros
