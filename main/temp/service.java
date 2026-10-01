package com.barclaycard.systemtest.emv.gateway.db;

import com.barclaycard.systemtest.emv.gateway.db.entity.TokenEventsEntity;
import com.barclaycard.systemtest.emv.gateway.db.mapper.TokenEventsMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.barclaycard.systemtest.emv.gateway.db.utils.JdbcUtils.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenDatabaseService {

    private static final String TOKEN_EVENTS_QUERY =
            "SELECT * " +
            "FROM EMV_TOKENISATION.TOKEN_EVENTS " +
            "WHERE %s = ?";


    public static List<TokenEventsEntity> getTokenEventsByCorrelationId(
            @NonNull Connection connection,
            @NonNull String correlationId
    ) throws SQLException {

        return getTokenEventsBy(
                connection,
                TokenEventColumn.CORRELATION_ID,
                correlationId
        );
    }


    public static List<TokenEventsEntity> getTokenEventsByTokenId(
            @NonNull Connection connection,
            @NonNull Integer tokenId
    ) throws SQLException {

        return getTokenEventsBy(
                connection,
                TokenEventColumn.TOKEN_ID,
                tokenId
        );
    }


    public static List<TokenEventsEntity> getTokenEventsByTokenOwnerId(
            @NonNull Connection connection,
            @NonNull String tokenOwnerId
    ) throws SQLException {

        return getTokenEventsBy(
                connection,
                TokenEventColumn.TOKEN_OWNER_ID,
                tokenOwnerId
        );
    }


    public static List<TokenEventsEntity> getTokenEventsByExternalReferenceId(
            @NonNull Connection connection,
            @NonNull String externalReferenceId
    ) throws SQLException {

        return getTokenEventsBy(
                connection,
                TokenEventColumn.EXTERNAL_REFERENCE_ID,
                externalReferenceId
        );
    }


    private static List<TokenEventsEntity> getTokenEventsBy(
            Connection connection,
            TokenEventColumn column,
            Object value
    ) throws SQLException {

        String query = String.format(
                TOKEN_EVENTS_QUERY,
                column.name()
        );

        try (var statement = connection.prepareStatement(query)) {

            statement.setObject(1, value);

            try (var resultSet = statement.executeQuery()) {
                return toList(
                        resultSet,
                        TokenEventsMapper.MAPPER
                );
            }
        }
    }


    private enum TokenEventColumn {

        CORRELATION_ID,
        TOKEN_ID,
        TOKEN_OWNER_ID,
        EXTERNAL_REFERENCE_ID
    }
}
