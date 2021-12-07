public final class Utils {
    public static String removeRoDiacritics(String text) {
        text = text.replaceAll("[ăâ]", "a");
        text = text.replaceAll("[ț]", "t");
        text = text.replaceAll("[ș]", "s");
        text = text.replaceAll("[î]", "i");

        return text;
    }
}
