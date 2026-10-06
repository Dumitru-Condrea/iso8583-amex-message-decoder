package your.package.api.wiremock;

import your.package.api.ApiClients;

public final class WireMockStateManager {

    private final WireMockClient client;

    /*
     * null  -> lifecycle не запускался
     * true  -> original state = true
     * false -> original state = false
     */
    private Boolean originalState;

    /*
     * true только если была предпринята
     * попытка изменить environment.
     */
    private boolean restoreRequired;

    private WireMockStateManager() {

        this.client =
                ApiClients.wiremock();
    }

    public static void apply(
            boolean requiredState) {

        Holder.INSTANCE
                .applyInternal(
                        requiredState
                );
    }

    public static void restore() {

        Holder.INSTANCE
                .restoreInternal();
    }

    private void applyInternal(
            boolean requiredState) {

        ensureCleanState();

        /*
         * Internally:
         *
         * REFRESH
         * GET
         */
        boolean currentState =
                client.getState();

        /*
         * Capture snapshot.
         */
        originalState =
                currentState;

        /*
         * Environment already has
         * the required state.
         */
        if (currentState == requiredState) {
            return;
        }

        /*
         * Set BEFORE updateState().
         *
         * If server applies SET but HTTP call
         * fails afterwards, @After will still
         * attempt to restore the snapshot.
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
         * apply() wasn't called.
         *
         * Safe no-op.
         */
        if (originalState == null) {
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

    private void ensureCleanState() {

        if (originalState != null) {

            throw new IllegalStateException(
                    "WireMock state snapshot "
                            + "already exists"
            );
        }
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
