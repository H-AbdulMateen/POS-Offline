package com.abdulmateen.cmpskeleton.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.abdulmateen.cmpskeleton.common.data.database.DatabaseFactory
import com.abdulmateen.cmpskeleton.core.data.datastore.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore(context = null) }
        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }

    }