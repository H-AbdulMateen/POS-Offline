package com.abdulmateen.cmpskeleton.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.abdulmateen.cmpskeleton.StartupViewModel
import com.abdulmateen.cmpskeleton.common.data.database.DatabaseFactory
import com.abdulmateen.cmpskeleton.common.data.database.MyAppDatabase
import com.abdulmateen.cmpskeleton.core.data.datastore.DataStoreManagerImpl
import com.abdulmateen.cmpskeleton.core.data.datastore.createDataStore
import com.abdulmateen.cmpskeleton.core.data.network.HttpClientFactory
import com.abdulmateen.cmpskeleton.core.domain.DataStoreManager
import com.abdulmateen.cmpskeleton.feature.auth.data.network.RemoteUserDataSource
import com.abdulmateen.cmpskeleton.feature.auth.data.network.ktor.KtorUserDataSource
import com.abdulmateen.cmpskeleton.feature.auth.domain.LoginRepository
import com.abdulmateen.cmpskeleton.feature.auth.presentation.login.LoginViewModel
import com.abdulmateen.cmpskeleton.feature.auth.presentation.register.SignUpViewModel
import com.abdulmateen.cmpskeleton.feature.auth.data.LoginRepositoryImpl
import com.abdulmateen.cmpskeleton.feature.main.home.data.ProductRepositoryImpl
import com.abdulmateen.cmpskeleton.feature.main.home.domain.ProductRepository
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.ktor.KtorProductsDataSource
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.ktor.RemoteProductsDataSource
import com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_list.ProductListViewModel
import com.abdulmateen.cmpskeleton.feature.main.settings.presentation.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { createDataStore(get()) }
    viewModelOf(::StartupViewModel)
    single { HttpClientFactory.create(get()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<MyAppDatabase>().productDao }

    singleOf(::KtorUserDataSource).bind<RemoteUserDataSource>()
    singleOf(::DataStoreManagerImpl).bind<DataStoreManager>()
    singleOf(::LoginRepositoryImpl).bind<LoginRepository>()
    singleOf(::KtorProductsDataSource).bind<RemoteProductsDataSource>()
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProductListViewModel)

}