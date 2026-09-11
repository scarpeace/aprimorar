package aprimorar.common.utils;

public final class ZipCodeUtils {

    private ZipCodeUtils() {}

    public static String normalize(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) return null;
        return zipCode.replaceAll("\\D", "");
    }

    public static String format(String zipCode) {
        if (zipCode == null || zipCode.length() != 8) return zipCode;

        return String.format(
            "%s-%s",
            zipCode.substring(0, 5),
            zipCode.substring(5, 8)
        );
    }
}
