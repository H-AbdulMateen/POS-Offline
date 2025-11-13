package com.abdulmateen.cmpskeleton.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val DATA_STORE_FILE_NAME = "my_prefs.preferences_pb"
fun createDataStorePref(producePath:()->String):DataStore<Preferences>{
    return PreferenceDataStoreFactory
        .createWithPath(
            produceFile = {
                producePath().toPath()
            }
        )
}
expect fun createDataStore(context:Any?):DataStore<Preferences>