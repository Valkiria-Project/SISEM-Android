package com.valkiria.uicomponents.utlis

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val AMERICA_BOGOTA_TIME_ZONE = "GMT-5"
const val MINUTES_SECONDS_12_HOURS_PATTERN = "hh:mm"

object TimeUtils {

    fun getLocalDateFromInstant(instant: Instant): LocalDate {
        return instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)).toLocalDate()
    }

    fun getFormattedLocalTimeAsString(
        localTime: LocalTime = LocalTime.now(),
        pattern: String = MINUTES_SECONDS_12_HOURS_PATTERN
    ): String = localTime.format(DateTimeFormatter.ofPattern(pattern))

    fun getLocalHourFromInstantAsInt(instant: Instant): Int {
        val localTime = LocalTime.from(instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)))
        return localTime.hour
    }

    fun getLocalMinutesFromInstantAsInt(instant: Instant): Int {
        val localTime = LocalTime.from(instant.atZone(ZoneId.of(AMERICA_BOGOTA_TIME_ZONE)))
        return localTime.minute
    }
}
