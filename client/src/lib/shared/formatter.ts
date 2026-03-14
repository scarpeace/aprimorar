export function onlyDigits(value: string): string {
    return value.replace(/\D/g, "")
}

export function formatCpf(value: string): string {
    const digits = onlyDigits(value).slice(0, 11)
    return digits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4")
}

export function isValidCpf(value: string): boolean {
    const digits = onlyDigits(value)

    if (digits.length !== 11) {
        return false
    }

    if (/^(\d)\1{10}$/.test(digits)) {
        return false
    }

    let sum = 0

    for (let index = 0; index < 9; index += 1) {
        sum += Number(digits[index]) * (10 - index)
    }

    let remainder = (sum * 10) % 11
    const firstVerifier = remainder === 10 ? 0 : remainder

    if (firstVerifier !== Number(digits[9])) {
        return false
    }

    sum = 0

    for (let index = 0; index < 10; index += 1) {
        sum += Number(digits[index]) * (11 - index)
    }

    remainder = (sum * 10) % 11
    const secondVerifier = remainder === 10 ? 0 : remainder

    return secondVerifier === Number(digits[10])
}

export function formatZip(value: string): string {
    const digits = onlyDigits(value).slice(0, 8)
    return digits.replace(/(\d{5})(\d{3})/, "$1-$2")
}

export function formatPhone(value: string): string {
    const digits = onlyDigits(value).slice(0, 11)

    if (digits.length <= 10) {
        return digits.replace(/(\d{2})(\d{4})(\d{4})/, "($1) $2-$3")
    }

    return digits.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3")
}

export function isValidBrazilianPhone(value: string): boolean {
    const digits = onlyDigits(value)

    if (digits.length !== 10 && digits.length !== 11) {
        return false
    }

    const areaCode = Number(digits.slice(0, 2))

    if (areaCode < 11 || areaCode > 99) {
        return false
    }

    if (/^(\d)\1+$/.test(digits)) {
        return false
    }

    if (digits.length === 11) {
        return digits[2] === "9"
    }

    return ["2", "3", "4", "5"].includes(digits[2])
}

function toValidDate(value: string | Date): Date | null {
    const date = value instanceof Date ? value : new Date(value)

    if (Number.isNaN(date.getTime())) {
        return null
    }

    return date
}

export function formatDateShortYear(value: string | Date): string {
    const date = toValidDate(value)

    if (!date) {
        return ""
    }

    return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "2-digit",
        year: "2-digit",
    }).format(date)
}

export function formatDateMonth(value: string | Date): string {
    const date = toValidDate(value)

    if (!date) {
        return ""
    }

    return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "2-digit",
    }).format(date)
}

export function formatTime(value: string | Date): string {
    const date = toValidDate(value)

    if (!date) {
        return ""
    }

    return new Intl.DateTimeFormat("pt-BR", {
        hour: "2-digit",
        minute: "2-digit",
    }).format(date)
}

export function formatMonth(value: string | Date): string {
    const date = toValidDate(value)

    if (!date) {
        return ""
    }

    return new Intl.DateTimeFormat("pt-BR", {
        month: "2-digit",
    }).format(date)
}

export const brl = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
})
