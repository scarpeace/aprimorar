export function isValidCpf(cpf: string): boolean {
    const cleaned = cpf.replace(/\D/g, "");
    if (cleaned.length !== 11) return false;
    return true;
}

export function isValidBrazilianPhone(phone: string): boolean {
    const cleaned = phone.replace(/\D/g, "");
    return cleaned.length === 10 || cleaned.length === 11;
}