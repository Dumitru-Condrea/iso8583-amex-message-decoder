private static void waitForConfiguration() {

    long seconds = Long.parseLong(
            Config.get("tgs.config.wait-seconds")
    );

    if (seconds < 0) {
        throw new IllegalArgumentException(
                "TGS configuration wait cannot be negative: "
                        + seconds
        );
    }

    try {
        TimeUnit.SECONDS.sleep(seconds);

    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();

        throw new IllegalStateException(
                "Interrupted while waiting for TGS configuration update",
                e
        );
    }
}
