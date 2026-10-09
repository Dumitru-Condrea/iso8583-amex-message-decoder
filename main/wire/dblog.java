@ParameterType("latest transaction by date|latest transaction")
    public TokenEventsFilter tokenEventsFilter(String value) {
        return value.endsWith("by date")
                ? TokenEventsFilter.LAST_TRANSACTION_DATE
                : TokenEventsFilter.LATEST_TRANSACTION;
    }

    @ParameterType("are only|are")
    public EventsMatchMode eventsMatchMode(String value) {
        return value.equals("are only") ? EventsMatchMode.ONLY : EventsMatchMode.CONTAINS;
    }
