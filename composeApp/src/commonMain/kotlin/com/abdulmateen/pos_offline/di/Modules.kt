package com.abdulmateen.pos_offline.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.abdulmateen.pos_offline.StartupViewModel
import com.abdulmateen.pos_offline.core.data.datastore.DataStoreManagerImpl
import com.abdulmateen.pos_offline.core.data.datastore.createDataStore
import com.abdulmateen.pos_offline.core.data.network.HttpClientFactory
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.data.database.DatabaseFactory
import com.abdulmateen.pos_offline.data.database.MyAppDatabase
import com.abdulmateen.pos_offline.data.repository.CartRepositoryImpl
import com.abdulmateen.pos_offline.feature.auth.data.LoginRepositoryImpl
import com.abdulmateen.pos_offline.feature.auth.data.network.RemoteUserDataSource
import com.abdulmateen.pos_offline.feature.auth.data.network.ktor.KtorUserDataSource
import com.abdulmateen.pos_offline.feature.auth.domain.LoginRepository
import com.abdulmateen.pos_offline.feature.auth.presentation.login.LoginViewModel
import com.abdulmateen.pos_offline.feature.auth.presentation.register.SignUpViewModel
import com.abdulmateen.pos_offline.feature.main.home.data.network.ktor.KtorProductsDataSource
import com.abdulmateen.pos_offline.feature.main.home.data.network.ktor.RemoteProductsDataSource
import com.abdulmateen.pos_offline.data.repository.InventoryRepositoryImpl
import com.abdulmateen.pos_offline.data.repository.OrderRepositoryImpl
import com.abdulmateen.pos_offline.domain.repository.CartRepository
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.domain.use_cases.cart.AddItemToCart
import com.abdulmateen.pos_offline.domain.use_cases.cart.CartUseCases
import com.abdulmateen.pos_offline.domain.use_cases.cart.ClearCartItems
import com.abdulmateen.pos_offline.domain.use_cases.cart.DecrementInQuantity
import com.abdulmateen.pos_offline.domain.use_cases.cart.GetCartItemCount
import com.abdulmateen.pos_offline.domain.use_cases.cart.GetCartItemList
import com.abdulmateen.pos_offline.domain.use_cases.cart.IncrementInQuantity
import com.abdulmateen.pos_offline.domain.use_cases.cart.RemoveItem
import com.abdulmateen.pos_offline.domain.use_cases.product.AddProduct
import com.abdulmateen.pos_offline.domain.use_cases.product.DeleteProduct
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductList
import com.abdulmateen.pos_offline.domain.use_cases.product.ProductUseCases
import com.abdulmateen.pos_offline.domain.use_cases.product.SearchProductByName
import com.abdulmateen.pos_offline.feature.main.home.presentation.order.OrderViewModel
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.InventoryViewModel
import com.abdulmateen.pos_offline.feature.main.profile.presentation.ProfileViewModel
import com.abdulmateen.pos_offline.feature.main.settings.presentation.SettingsViewModel
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
    single { get<MyAppDatabase>().productDao() }
    single { get<MyAppDatabase>().orderDao() }
    single { get<MyAppDatabase>().categoryDao() }
    single { get<MyAppDatabase>().unitDao() }
    single { get<MyAppDatabase>().cartDao() }

    singleOf(::KtorUserDataSource).bind<RemoteUserDataSource>()
    singleOf(::DataStoreManagerImpl).bind<DataStoreManager>()
    singleOf(::LoginRepositoryImpl).bind<LoginRepository>()
    singleOf(::KtorProductsDataSource).bind<RemoteProductsDataSource>()
    singleOf(::InventoryRepositoryImpl).bind<InventoryRepository>()
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()

    single {
        ProductUseCases(
            searchProduct = SearchProductByName(get()),
            getProductList = GetProductList(get()),
            addProduct = AddProduct(get()),
            deleteProduct = DeleteProduct(get())
        )
    }

    single {
        CartUseCases(
            getCartItemList = GetCartItemList(get()),
            getCartItemCount = GetCartItemCount(get()),
            addItemToCart = AddItemToCart(get()),
            removeItem = RemoveItem(get()),
            clearCartItems = ClearCartItems(get()),
            incrementInQuantity = IncrementInQuantity(get()),
            decrementInQuantity = DecrementInQuantity(get())
        )
    }

    viewModelOf(::StartupViewModel)

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::InventoryViewModel)
    viewModelOf(::OrderViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)

}