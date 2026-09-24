private static String loadQueryTemplate(String resource) {
    ClassLoader classLoader =
            TokenCleanupService.class.getClassLoader();

    try (InputStream inputStream =
                 classLoader.getResourceAsStream(resource)) {

        if (inputStream == null) {
            throw new IllegalStateException(
                    "SQL resource not found: " + resource
            );
        }

        return IOUtils.toString(
                inputStream,
                StandardCharsets.UTF_8
        );
    }
}