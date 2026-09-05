package com.abdulmateen.pos_offline.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.abdulmateen.pos_offline.core.data.datastore.createDataStore
import com.abdulmateen.pos_offline.core.data.export.JvmDataExporter
import com.abdulmateen.pos_offline.core.data.filestorage.ImageStorage
import com.abdulmateen.pos_offline.core.domain.export.DataExporter
import com.abdulmateen.pos_offline.data.database.DatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore(context = null) }
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory() }
        single { ImageStorage() }
        single { JvmDataExporter() }.bind<DataExporter>()
    }
