package aprimorar.common.utils;

public final class EmailUtils {

    private EmailUtils() {}

    public static String normalize(String email) {
        if (email == null || email.isBlank()) return null;
        return email.trim().toLowerCase();
    }
}
