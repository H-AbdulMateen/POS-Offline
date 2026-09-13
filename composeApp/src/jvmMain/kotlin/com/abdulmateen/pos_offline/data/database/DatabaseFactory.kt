package com.abdulmateen.pos_offline.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<MyAppDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA") ?: userHome, "POSOffline")
            os.contains("mac") -> File(userHome, "Library/Application Support/POSOffline")
            else -> File(userHome, ".local/share/POSOffline")
        }

        if(!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, MyAppDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}