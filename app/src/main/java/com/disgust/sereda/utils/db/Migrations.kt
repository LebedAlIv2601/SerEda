package com.disgust.sereda.utils.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `filters_recipes` " +
                        "(`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `image` TEXT, " +
                        "`isInclude` INTEGER NOT NULL, PRIMARY KEY (`id`))"
            )
        }
    }
}