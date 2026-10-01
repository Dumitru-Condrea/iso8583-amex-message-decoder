private static final ResultSetMapper<TokenEntity> TOKEN_MAPPER =
        rs -> TokenEntity.builder()
                .id(rs.getLong("ID"))
                .tokenOwnerId(rs.getString("TOKEN_OWNER_ID"))
                .sourcePciToken(rs.getString("SOURCE_PCI_TOKEN"))
                .status(rs.getString("STATUS"))
                .createdDate(rs.getTimestamp("CREATED_DATE"))
                // ... все остальные колонки
                .build();
