package com.aprimorar.api.shared;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MapperUtils {

    public static final String CPF = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
    public static final String PHONE_BR = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$";
    public static final String ZIP_CODE_BR = "^\\d{5}-?\\d{3}$";

    public static final String CPF_MESSAGE = "CPF deve estar no formato XXX.XXX.XXX-XX";
    public static final String PHONE_BR_MESSAGE = "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX";
    public static final String ZIP_CODE_BR_MESSAGE = "CEP deve estar no formato 00000-000 ou 00000000";


    public static String sanitizeCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) return null;
        return cpf.replaceAll("\\D", "");
    }

    public static String sanitizeEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return email.trim().toLowerCase();
    }

    public static String sanitizeContact(String contact) {
        if (contact == null || contact.isBlank()) return null;
        return contact.replaceAll("\\D", "");
    }

    public static String sanitizeZip(String zip) {
        if (zip == null || zip.isBlank()) return null;
        return zip.replaceAll("\\D", "");
    }

    public static String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }

    public static String formatContact(String contact) {
        if (contact == null || contact.length() != 11) return contact;
        return String.format("(%s)%s-%s",
                contact.substring(0, 2),
                contact.substring(2, 7),
                contact.substring(7, 11));
    }

    public static String formatZip(String zip) {
        if (zip == null || zip.length() != 8) return zip;
        return String.format("%s-%s", zip.substring(0, 5), zip.substring(5, 8));
    }

    public static String jsonAsString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
