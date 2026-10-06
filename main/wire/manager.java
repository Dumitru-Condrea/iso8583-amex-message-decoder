package your.package.api.wiremock;

import your.package.api.ApiClients;

public final class WireMockStateManager {

    private final WireMockClient client;

    /*
     * null  -> snapshot ещё не был создан
     * true  -> original WireMock state = true
     * false -> original WireMock state = false
     */
    private Boolean originalState;

    /*
     * Показывает, выполнялось ли хотя бы одно
     * изменение WireMock state после snapshot.
     */
    private boolean restoreRequired;

    private WireMockStateManager() {
        this.client = ApiClients.wiremock();
    }

    public static void apply(
            boolean requiredState) {

        Holder.INSTANCE.applyInternal(
                requiredState
        );
    }

    public static void restore() {

        Holder.INSTANCE.restoreInternal();
    }

    private void applyInternal(
            boolean requiredState) {

        /*
         * Internally:
         *
         * REFRESH
         * GET
         */
        boolean currentState =
                client.getState();

        /*
         * Capture only the very first state.
         *
         * Repeated apply() calls must not overwrite
         * the original snapshot.
         */
        if (!hasSnapshot()) {
            originalState = currentState;
        }

        /*
         * Nothing needs to be changed.
         */
        if (currentState == requiredState) {
            return;
        }

        /*
         * Mark before the HTTP operation.
         *
         * If SET succeeds on the server but the client
         * fails afterwards, restore() will still attempt
         * to return the original state.
         */
        restoreRequired = true;

        /*
         * Internally:
         *
         * SET requiredState
         * REFRESH
         */
        client.updateState(
                requiredState
        );
    }

    private void restoreInternal() {

        /*
         * apply() was never called.
         *
         * For example, scenario had no
         * @wiremock.active:* tag.
         */
        if (!hasSnapshot()) {
            return;
        }

        try {

            if (restoreRequired) {

                /*
                 * Internally:
                 *
                 * SET originalState
                 * REFRESH
                 */
                client.updateState(
                        originalState
                );
            }

        } finally {

            clear();
        }
    }

    private boolean hasSnapshot() {
        return originalState != null;
    }

    private void clear() {

        originalState = null;
        restoreRequired = false;
    }

    private static class Holder {

        private static final WireMockStateManager INSTANCE =
                new WireMockStateManager();
    }
}
