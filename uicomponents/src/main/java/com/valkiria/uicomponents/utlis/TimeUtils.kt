package com.valkiria.uicomponents.utlis

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val AMERICA_BOGOTA_TIME_ZONE = "GMT-5"
const val AMERICA_BOGOTA_ZONE = "America/Bogota"
const val HOURS_MINUTES_SECONDS_24_HOURS_PATTERN = "HH:mm:ss"
const val HOURS_MINUTES_24_HOURS_PATTERN = "HH:mm"
const val HOURS_MINUTES_12_HOURS_PATTERN = "hh:mm a"
const val DATE_PATTERN = "dd/MM/yyyy"
const val UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
const val WEEK_DAYS = 7

object TimeUtils {

    fun getLocalDate(instant: Instant): LocalDate {
        return instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)).toLocalDate()
    }

    fun getLocalDate(dateStringValue: String): LocalDate = LocalDate.parse(
        dateStringValue,
        DateTimeFormatter.ofPattern(DATE_PATTERN)
    )

    fun getLocalDateFromUTC(dateStringValue: String): String = LocalDate.parse(
        dateStringValue,
        DateTimeFormatter.ofPattern(UTC_DATE_PATTERN)
    ).format(DateTimeFormatter.ofPattern(DATE_PATTERN))

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
}
