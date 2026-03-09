package com.aprimorar.api.validation;

public final class ValidationPatterns {

    public static final String CPF = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
    public static final String PHONE_BR = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$";
    public static final String ZIP_CODE_BR = "^\\d{5}-?\\d{3}$";

    public static final String CPF_MESSAGE = "CPF deve estar no formato XXX.XXX.XXX-XX";
    public static final String PHONE_BR_MESSAGE = "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX";
    public static final String ZIP_CODE_BR_MESSAGE = "CEP deve estar no formato 00000-000 ou 00000000";

    private ValidationPatterns() {
    }
}
