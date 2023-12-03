package com.skgtecnologia.sisem.domain.stretcherretention.errors

sealed class StretchRetentionErrors : Exception() {
    data object NoIncidentId : StretchRetentionErrors()
}