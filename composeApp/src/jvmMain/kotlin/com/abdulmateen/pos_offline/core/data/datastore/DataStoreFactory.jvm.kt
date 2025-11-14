package com.abdulmateen.pos_offline.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual fun createDataStore(context: Any?): DataStore<Preferences>  = createDataStorePref (
    producePath = {
        val file = File(System.getProperty("java.io.tmpdir"), DATA_STORE_FILE_NAME)
        file.absolutePath
    }
)