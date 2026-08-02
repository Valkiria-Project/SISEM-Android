package com.skgtecnologia.sisem.commons.logging

import android.content.Context
import android.os.Environment
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val LOG_FOLDER = "SISEM-Logs"
private const val MAX_DAYS = 10L
private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

class FileLoggingTree(context: Context) : Timber.Tree() {

    // Saved under Downloads/SISEM-Logs/ so any file manager can find it.
    // Falls back to internal storage if external is not available.
    private val logDir: File = run {
        val external = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val dir = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File(external, LOG_FOLDER)
        } else {
            File(context.filesDir, LOG_FOLDER)
        }
        dir.also { it.mkdirs() }
    }

    init {
        purgeOldLogs()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.DEBUG) return

        val level = when (priority) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "?"
        }

        val timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT)
        val entry = buildString {
            append("$timestamp $level/$tag: $message")
            if (t != null) {
                append("\n")
                append(t.stackTraceToString())
            }
            append("\n")
        }

        val logFile = File(logDir, "${LocalDate.now().format(DATE_FORMAT)}.log")
        runCatching {
            FileWriter(logFile, true).use { it.write(entry) }
        }
    }

    private fun purgeOldLogs() {
        val cutoff = LocalDate.now().minusDays(MAX_DAYS)
        logDir.listFiles()
            ?.filter { file ->
                runCatching {
                    LocalDate.parse(file.nameWithoutExtension, DATE_FORMAT).isBefore(cutoff)
                }.getOrDefault(false)
            }
            ?.forEach { it.delete() }
    }
}
