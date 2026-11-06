package com.example.servidoriot

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "iot_database.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE registros (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "temperatura TEXT, " +
                    "puerta TEXT, " +
                    "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS registros")
        onCreate(db)
    }
}