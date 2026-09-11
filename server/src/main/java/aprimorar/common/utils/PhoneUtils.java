package aprimorar.common.utils;

public final class PhoneUtils {

    private PhoneUtils() {}

    public static String normalize(String phone) {
        if (phone == null || phone.isBlank()) return null;
        return phone.replaceAll("\\D", "");
    }

    public static String format(String phone) {
        if (phone == null || phone.length() != 11) return phone;

        return String.format(
            "(%s)%s-%s",
            phone.substring(0, 2),
            phone.substring(2, 7),
            phone.substring(7, 11)
        );
    }
}
