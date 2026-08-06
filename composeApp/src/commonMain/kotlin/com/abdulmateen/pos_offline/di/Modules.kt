package com.abdulmateen.pos_offline.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.abdulmateen.pos_offline.StartupViewModel
import com.abdulmateen.pos_offline.core.data.datastore.DataStoreManagerImpl
import com.abdulmateen.pos_offline.core.data.network.HttpClientFactory
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.data.database.DatabaseFactory
import com.abdulmateen.pos_offline.data.database.MyAppDatabase
import com.abdulmateen.pos_offline.data.repository.CartRepositoryImpl
import com.abdulmateen.pos_offline.data.repository.DashboardRepositoryImpl
import com.abdulmateen.pos_offline.feature.auth.data.LoginRepositoryImpl
import com.abdulmateen.pos_offline.feature.auth.data.network.RemoteUserDataSource
import com.abdulmateen.pos_offline.feature.auth.data.network.ktor.KtorUserDataSource
import com.abdulmateen.pos_offline.feature.auth.domain.LoginRepository
import com.abdulmateen.pos_offline.feature.auth.presentation.login.LoginViewModel
import com.abdulmateen.pos_offline.feature.auth.presentation.register.SignUpViewModel
import com.abdulmateen.pos_offline.feature.dashboard.presentation.DashboardViewModel
import com.abdulmateen.pos_offline.data.repository.InventoryRepositoryImpl
import com.abdulmateen.pos_offline.data.repository.OrderRepositoryImpl
import com.abdulmateen.pos_offline.domain.repository.CartRepository
import com.abdulmateen.pos_offline.domain.repository.DashboardRepository
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.domain.use_cases.cart.AddItemToCart
import com.abdulmateen.pos_offline.domain.use_cases.cart.CalculateSubTotal
import com.abdulmateen.pos_offline.domain.use_cases.CartUseCases
import com.abdulmateen.pos_offline.domain.use_cases.cart.ClearCartItems
import com.abdulmateen.pos_offline.domain.use_cases.cart.DecrementInQuantity
import com.abdulmateen.pos_offline.domain.use_cases.cart.GetCartItemCount
import com.abdulmateen.pos_offline.domain.use_cases.cart.GetCartItemList
import com.abdulmateen.pos_offline.domain.use_cases.cart.IncrementInQuantity
import com.abdulmateen.pos_offline.domain.use_cases.cart.RemoveItem
import com.abdulmateen.pos_offline.domain.use_cases.product.AddProduct
import com.abdulmateen.pos_offline.domain.use_cases.product.DeleteProduct
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductList
import com.abdulmateen.pos_offline.domain.use_cases.ProductUseCases
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductDetail
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductUiList
import com.abdulmateen.pos_offline.domain.use_cases.product.ReduceStock
import com.abdulmateen.pos_offline.domain.use_cases.product.SearchProductByName
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderViewModel
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryViewModel
import com.abdulmateen.pos_offline.feature.main.MainViewModel
import com.abdulmateen.pos_offline.feature.profile.presentation.ProfileViewModel
import com.abdulmateen.pos_offline.feature.settings.presentation.SettingsViewModel
import com.abdulmateen.pos_offline.domain.repository.ExpenseRepository
import com.abdulmateen.pos_offline.data.repository.ExpenseRepositoryImpl
import com.abdulmateen.pos_offline.feature.expense.presentation.ExpenseViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModelOf(::StartupViewModel)
    single { HttpClientFactory.create(get()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<MyAppDatabase>().productDao() }
    single { get<MyAppDatabase>().orderDao() }
    single { get<MyAppDatabase>().categoryDao() }
    single { get<MyAppDatabase>().unitDao() }
    single { get<MyAppDatabase>().cartDao() }
    single { get<MyAppDatabase>().dashboardDao() }
    single { get<MyAppDatabase>().employeeDao() }
    single { get<MyAppDatabase>().expenseDao() }

    singleOf(::KtorUserDataSource).bind<RemoteUserDataSource>()
    singleOf(::DataStoreManagerImpl).bind<DataStoreManager>()
    singleOf(::LoginRepositoryImpl).bind<LoginRepository>()
    singleOf(::InventoryRepositoryImpl).bind<InventoryRepository>()
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()
    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()
    singleOf(::ExpenseRepositoryImpl).bind<ExpenseRepository>()

    single {
        ProductUseCases(
            searchProduct = SearchProductByName(get()),
            getProductList = GetProductList(get()),
            addProduct = AddProduct(get()),
            deleteProduct = DeleteProduct(get()),
            reduceStock = ReduceStock(get()),
            getProductDetail = GetProductDetail(get()),
            getProductUiList = GetProductUiList(get())
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
            decrementInQuantity = DecrementInQuantity(get()),
            calculateSubTotal = CalculateSubTotal(get())
        )
    }

    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::InventoryViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::OrderViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ExpenseViewModel)

}