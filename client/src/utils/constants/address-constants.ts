export const addressConstants = {
  AC: "AC",
  AL: "AL",
  AP: "AP",
  AM: "AM",
  BA: "BA",
  CE: "CE",
  DF: "DF",
  ES: "ES",
  GO: "GO",
  MA: "MA",
  MT: "MT",
  MS: "MS",
  MG: "MG",
  PA: "PA",
  PB: "PB",
  PR: "PR",
  PE: "PE",
  PI: "PI",
  RJ: "RJ",
  RN: "RN",
  RS: "RS",
  RO: "RO",
  RR: "RR",
  SC: "SC",
  SP: "SP",
  SE: "SE",
  TO: "TO",
} as const;

export type BrazilianState = keyof typeof addressConstants;

export const BRAZILIAN_STATES = Object.entries(addressConstants).map(([value, label]) => ({
  value,
  label,
}));

export const BRAZILIAN_STATE_VALUES = Object.values(addressConstants);
