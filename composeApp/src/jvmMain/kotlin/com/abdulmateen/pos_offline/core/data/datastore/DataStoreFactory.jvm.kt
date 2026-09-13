package com.abdulmateen.pos_offline.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual fun createDataStore(context: Any?): DataStore<Preferences> = createDataStorePref(
    producePath = {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA") ?: userHome, "POSOffline")
            os.contains("mac") -> File(userHome, "Library/Application Support/POSOffline")
            else -> File(userHome, ".local/share/POSOffline")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        
        File(appDataDir, DATA_STORE_FILE_NAME).absolutePath
    }
)
