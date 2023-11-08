package com.valkiria.uicomponents.utlis

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val AMERICA_BOGOTA_TIME_ZONE = "GMT-5"
const val AMERICA_BOGOTA_ZONE = "America/Bogota"
const val HOURS_MINUTES_24_HOURS_PATTERN = "HH:mm"
const val WEEK_DAYS = 7

object TimeUtils {

    fun getLocalDateFromInstant(instant: Instant): LocalDate {
        return instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)).toLocalDate()
    }

    fun getFormattedLocalTimeAsString(
        localTime: LocalTime = LocalTime.now(),
        pattern: String = HOURS_MINUTES_24_HOURS_PATTERN
    ): String = localTime.format(DateTimeFormatter.ofPattern(pattern))

    fun getLocalDateInMillis(selectedDate: LocalDate): Long {
        return selectedDate
            .atStartOfDay()
            .atZone(ZoneId.of(AMERICA_BOGOTA_ZONE))
            .toInstant()
            .toEpochMilli()
    }
}
