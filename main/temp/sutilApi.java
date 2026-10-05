/*
 * EntityUtils API
 *
 * Generic filtering and sorting for any Entity with getters.
 * Java 8.
 *
 * Recommended static imports:
 *
 * import static com...db.utils.EntityUtils.by;
 * import static com...db.utils.EntityUtils.comparing;
 * import static com...db.utils.EntityUtils.comparingDescending;
 * import static java.util.stream.Collectors.toList;
 */


/* =========================================================
 * FILTER
 * ========================================================= */

// String
var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getCorrelationId,
                correlationId
        ))
        .collect(toList());


// Integer
var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getTokenId,
                12617
        ))
        .collect(toList());


// Enum
var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getState,
                TokenState.ACTIVE
        ))
        .collect(toList());


// YearMonth
var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getTokenExpiryDate,
                YearMonth.of(2030, 12)
        ))
        .collect(toList());


// LocalDateTime
var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getCreatedDateTime,
                expectedDateTime
        ))
        .collect(toList());


// Filter by NULL
var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getExternalReferenceId,
                null
        ))
        .collect(toList());


/* =========================================================
 * MULTIPLE FILTERS
 * ========================================================= */

var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getState,
                TokenState.ACTIVE
        ))
        .filter(by(
                TokenEventsEntity::getScheme,
                CardScheme.VISA
        ))
        .filter(by(
                TokenEventsEntity::getTokenOwnerId,
                ownerId
        ))
        .collect(toList());


/* =========================================================
 * SORT ASCENDING
 * NULL values are placed last
 * ========================================================= */

// Integer
var result = entities.stream()
        .sorted(comparing(
                TokenEventsEntity::getTokenId
        ))
        .collect(toList());


// String
var result = entities.stream()
        .sorted(comparing(
                TokenEventsEntity::getTokenOwnerId
        ))
        .collect(toList());


// LocalDateTime
var result = entities.stream()
        .sorted(comparing(
                TokenEventsEntity::getCreatedDateTime
        ))
        .collect(toList());


// YearMonth
var result = entities.stream()
        .sorted(comparing(
                TokenEventsEntity::getTokenExpiryDate
        ))
        .collect(toList());


/* =========================================================
 * SORT DESCENDING
 * ========================================================= */

var result = entities.stream()
        .sorted(comparingDescending(
                TokenEventsEntity::getCreatedDateTime
        ))
        .collect(toList());


/* =========================================================
 * FILTER + SORT
 * ========================================================= */

var result = entities.stream()
        .filter(by(
                TokenEventsEntity::getState,
                TokenState.ACTIVE
        ))
        .filter(by(
                TokenEventsEntity::getScheme,
                CardScheme.VISA
        ))
        .sorted(comparingDescending(
                TokenEventsEntity::getCreatedDateTime
        ))
        .collect(toList());


/* =========================================================
 * GET FIRST MATCH
 * ========================================================= */

TokenEventsEntity event = entities.stream()
        .filter(by(
                TokenEventsEntity::getTokenId,
                tokenId
        ))
        .findFirst()
        .orElse(null);


/* =========================================================
 * CHECK IF EXISTS
 * ========================================================= */

boolean exists = entities.stream()
        .anyMatch(by(
                TokenEventsEntity::getTokenId,
                tokenId
        ));


/* =========================================================
 * COUNT
 * ========================================================= */

long count = entities.stream()
        .filter(by(
                TokenEventsEntity::getState,
                TokenState.ACTIVE
        ))
        .count();
