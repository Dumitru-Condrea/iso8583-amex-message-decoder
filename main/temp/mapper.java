package com.barclaycard.systemtest.emv.gateway.db.mapper;

import com.barclaycard.systemtest.emv.gateway.db.entity.TokenEventsEntity;
import com.barclaycard.systemtest.emv.gateway.db.enums.CardScheme;
import com.barclaycard.systemtest.emv.gateway.db.enums.EventType;
import com.barclaycard.systemtest.emv.gateway.db.enums.TokenState;
import com.barclaycard.systemtest.emv.gateway.db.utils.ResultSetMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.barclaycard.systemtest.emv.gateway.db.utils.EntityMapperUtils.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenEventsMapper {

    public static final ResultSetMapper<TokenEventsEntity> MAPPER =
            resultSet -> TokenEventsEntity.builder()
                    .tokenOwnerId(
                            resultSet.getString("TOKEN_OWNER_ID")
                    )
                    .tokenId(
                            resultSet.getInt("TOKEN_ID")
                    )
                    .externalReferenceId(
                            resultSet.getString("EXTERNAL_REFERENCE_ID")
                    )
                    .eventType(
                            getEnum(
                                    resultSet.getString("EVENT_TYPE"),
                                    EventType.class
                            )
                    )
                    .scheme(
                            getEnum(
                                    resultSet.getString("SCHEME"),
                                    CardScheme.class
                            )
                    )
                    .createdDateTime(
                            getLocalDateTime(
                                    resultSet,
                                    "CREATED_DATE_TIME"
                            )
                    )
                    .correlationId(
                            resultSet.getString("CORRELATION_ID")
                    )
                    .plainNetworkToken(
                            resultSet.getString("PLAIN_NETWORK_TOKEN")
                    )
                    .state(
                            getEnum(
                                    resultSet.getString("STATE"),
                                    TokenState.class
                            )
                    )
                    .tokenExpiryDate(
                            getYearMonth(
                                    resultSet,
                                    "TOKEN_EXPIRY_DATE"
                            )
                    )
                    .build();

    private TokenEventsMapper() {
    }
}
