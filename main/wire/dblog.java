public static String separator(String text) {
    int maxLength = Arrays.stream(text.split("\\R"))
            .mapToInt(String::length)
            .max()
            .orElse(0);

    char[] chars = new char[maxLength + 3];
    Arrays.fill(chars, '=');

    return new String(chars);
}
