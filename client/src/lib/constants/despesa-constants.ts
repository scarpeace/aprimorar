import {
  despesaRequestCategoriaEnum,
  despesaRequestFormaPagamentoEnum,
} from "@/lib/api/generated/types/DespesaRequest";

export const categoriaDespesaLabels: Record<string, string> = {
  CONTAS: "Contas",
  PROFESSORES: "Professores",
  FUNCIONARIOS: "Funcionários",
  DESPENSA: "Despensa",
  MANUTENCAO: "Manutenção",
  SERVICOS: "Serviços",
  ASSINATURAS: "Assinaturas",
};

export const formaPagamentoDespesaLabels: Record<string, string> = {
  PIX: "Pix",
  DINHEIRO: "Dinheiro",
  CARTAO_CREDITO: "Cartão de crédito",
  CARTAO_DEBITO: "Cartão de débito",
  BOLETO: "Boleto",
  TRANSFERENCIA: "Transferência",
};

export const categoriaDespesaOptions = [
  { value: "", label: "Todas as categorias" },
  { value: despesaRequestCategoriaEnum.CONTAS, label: categoriaDespesaLabels.CONTAS },
  { value: despesaRequestCategoriaEnum.PROFESSORES, label: categoriaDespesaLabels.PROFESSORES },
  { value: despesaRequestCategoriaEnum.FUNCIONARIOS, label: categoriaDespesaLabels.FUNCIONARIOS },
  { value: despesaRequestCategoriaEnum.DESPENSA, label: categoriaDespesaLabels.DESPENSA },
  { value: despesaRequestCategoriaEnum.MANUTENCAO, label: categoriaDespesaLabels.MANUTENCAO },
  { value: despesaRequestCategoriaEnum.SERVICOS, label: categoriaDespesaLabels.SERVICOS },
  { value: despesaRequestCategoriaEnum.ASSINATURAS, label: categoriaDespesaLabels.ASSINATURAS },
] as const;

export const formaPagamentoDespesaOptions = [
  { value: "", label: "Todas as formas" },
  { value: despesaRequestFormaPagamentoEnum.PIX, label: formaPagamentoDespesaLabels.PIX },
  { value: despesaRequestFormaPagamentoEnum.DINHEIRO, label: formaPagamentoDespesaLabels.DINHEIRO },
  { value: despesaRequestFormaPagamentoEnum.CARTAO_CREDITO, label: formaPagamentoDespesaLabels.CARTAO_CREDITO },
  { value: despesaRequestFormaPagamentoEnum.CARTAO_DEBITO, label: formaPagamentoDespesaLabels.CARTAO_DEBITO },
  { value: despesaRequestFormaPagamentoEnum.BOLETO, label: formaPagamentoDespesaLabels.BOLETO },
  { value: despesaRequestFormaPagamentoEnum.TRANSFERENCIA, label: formaPagamentoDespesaLabels.TRANSFERENCIA },
] as const;

export const createCategoriaDespesaOptions = categoriaDespesaOptions.filter((option) => option.value !== "");
export const createFormaPagamentoDespesaOptions = formaPagamentoDespesaOptions.filter((option) => option.value !== "");
