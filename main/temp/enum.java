package com.barclaycard.systemtest.db.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumMapper {

    public static <E extends Enum<E>> E fromDbValue(
            String value,
            Class<E> enumClass
    ) {
        if (value == null) {
            return null;
        }

        return Enum.valueOf(
                enumClass,
                value.trim().toUpperCase(Locale.ROOT)
        );
    }
}
