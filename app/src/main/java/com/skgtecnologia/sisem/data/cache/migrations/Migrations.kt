package com.skgtecnologia.sisem.data.cache.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds the encrypted-credential and inactivity columns to biometric_credentials so biometric
 * login can silently re-authenticate. A real (non-destructive) migration is used on purpose:
 * the biometric enrollment data must survive app updates.
 */
val MIGRATION_24_25 = object : Migration(24, 25) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE biometric_credentials ADD COLUMN encryptedPassword TEXT NOT NULL DEFAULT ''"
        )
        db.execSQL(
            "ALTER TABLE biometric_credentials ADD COLUMN credentialIv TEXT NOT NULL DEFAULT ''"
        )
        db.execSQL(
            "ALTER TABLE biometric_credentials ADD COLUMN lastLoginAt INTEGER NOT NULL DEFAULT 0"
        )
    }
}
