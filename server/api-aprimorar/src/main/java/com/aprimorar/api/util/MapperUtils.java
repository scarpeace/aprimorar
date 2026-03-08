package com.aprimorar.api.util;

import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

    public String sanitizeCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) return null;
        return cpf.replaceAll("\\D", "");
    }

    public String sanitizeEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return email.trim().toLowerCase();
    }

    public String sanitizeContact(String contact) {
        if (contact == null || contact.isBlank()) return null;
        return contact.replaceAll("\\D", "");
    }

    public String sanitizeZip(String zip) {
        if (zip == null || zip.isBlank()) return null;
        return zip.replaceAll("\\D", "");
    }

    public String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }

    public String formatContact(String contact) {
        if (contact == null || contact.length() != 11) return contact;
        return String.format("(%s)%s-%s",
                contact.substring(0, 2),
                contact.substring(2, 7),
                contact.substring(7, 11));
    }

    public String formatZip(String zip) {
        if (zip == null || zip.length() != 8) return zip;
        return String.format("%s-%s", zip.substring(0, 5), zip.substring(5, 8));
    }
}
