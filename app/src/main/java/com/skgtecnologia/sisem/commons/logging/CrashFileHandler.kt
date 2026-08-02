package com.skgtecnologia.sisem.commons.logging

import timber.log.Timber

/**
 * Installs a global uncaught-exception handler that logs the full crash stack
 * trace via Timber (picked up by [FileLoggingTree]) before delegating to the
 * system default handler. This ensures crashes are captured in the daily log
 * files even when the process is about to be killed.
 */
object CrashFileHandler {

    fun install() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "UNCAUGHT EXCEPTION on thread ${thread.name}")
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
