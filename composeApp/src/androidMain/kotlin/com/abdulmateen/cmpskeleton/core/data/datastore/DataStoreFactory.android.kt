package com.abdulmateen.cmpskeleton.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> = createDataStorePref(
    producePath = { (context as Context).filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
)