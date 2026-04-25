package com.valkiria.uicomponents.utlis

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val AMERICA_BOGOTA_TIME_ZONE = "GMT-5"
const val AMERICA_BOGOTA_ZONE = "America/Bogota"
const val HOURS_MINUTES_SECONDS_24_HOURS_PATTERN = "HH:mm:ss"
const val HOURS_MINUTES_24_HOURS_PATTERN = "HH:mm"
const val HOURS_MINUTES_12_HOURS_PATTERN = "hh:mm a"
const val DATE_PATTERN = "dd/MM/yyyy"
const val DATE_TIME_PATTERN = "dd/MM/yyyy - HH : mm"
const val DATE_TIME_PATTERN_US = "yyyy-MM-dd HH:mm:ss"
const val UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
const val WEEK_DAYS = 7

@Suppress("TooManyFunctions")
object TimeUtils {

    fun getLocalDateTime(dateTime: String): LocalDateTime = LocalDateTime.parse(
        dateTime,
        DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_US)
    )

    fun getLocalDateTime(instant: Instant): LocalDateTime {
        return instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)).toLocalDateTime()
    }

    fun getLocalDate(instant: Instant): LocalDate {
        return instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)).toLocalDate()
    }

    fun getLocalDate(dateStringValue: String): LocalDate = LocalDate.parse(
        dateStringValue,
        DateTimeFormatter.ofPattern(DATE_PATTERN)
    )

    fun getLocalDateFromUTC(dateStringValue: String): String = runCatching {
        LocalDate.parse(
            dateStringValue,
            DateTimeFormatter.ofPattern(UTC_DATE_PATTERN)
        ).format(DateTimeFormatter.ofPattern(DATE_PATTERN))
    }.fold(
        onSuccess = { it },
        onFailure = { dateStringValue.substringBefore("T") }
    )

    fun getLocalTimeAsString(
        timeStringValue: String,
        pattern: String = HOURS_MINUTES_12_HOURS_PATTERN
    ): String = LocalTime.parse(
        timeStringValue, DateTimeFormatter.ofPattern(HOURS_MINUTES_SECONDS_24_HOURS_PATTERN)
    ).format(DateTimeFormatter.ofPattern(pattern))

    fun getLocalTimeAsString(
        localTime: LocalTime = LocalTime.now()
    ): String = localTime.format(DateTimeFormatter.ofPattern(HOURS_MINUTES_24_HOURS_PATTERN))

    fun getFormattedLocalTimeAsString(
        localTime: LocalTime = LocalTime.now(),
        pattern: String = HOURS_MINUTES_12_HOURS_PATTERN
    ): String = localTime.format(DateTimeFormatter.ofPattern(pattern))

    fun getLocalDateInMillis(selectedDate: LocalDate): Long = selectedDate
        .atStartOfDay()
        .atZone(ZoneId.of(AMERICA_BOGOTA_ZONE))
        .toInstant()
        .toEpochMilli()

    fun getEpochMillis(dateStringValue: String): Long = LocalDate.parse(dateStringValue)
        .atStartOfDay(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE))
        .toInstant()
        .toEpochMilli()

    fun getLocalTimeFromHhMmString(timeStringValue: String): LocalTime = LocalTime.parse(
        timeStringValue,
        DateTimeFormatter.ofPattern(HOURS_MINUTES_24_HOURS_PATTERN)
    )

    fun isSameDay(dateStringValue: String): Boolean {
        val localDate = getLocalDate(dateStringValue)
        return localDate.isEqual(LocalDate.now())
    }
}
