package com.valkiria.uicomponents.utlis

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private const val UTC_TIME_ZONE = "GMT+0"

object TimeUtils {

    fun getLocalDateFromInstant(instant: Instant): LocalDate {
        return instant.atZone(ZoneId.of(UTC_TIME_ZONE)).toLocalDate()
    }
}
