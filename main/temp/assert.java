assertThat(events)
        .extracting(TokenEventsEntity::getCreatedDateTime)
        .allSatisfy(date ->
                assertThat(date)
                        .describedAs(
                                "Token event date [%s] should be on or after transaction date [%s]",
                                date,
                                transactionDate
                        )
                        .isAfterOrEqualTo(transactionDate)
        );
