package com.abdulmateen.pos_offline.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdulmateen.pos_offline.data.database.MyAppDatabaseConstructor
import com.abdulmateen.pos_offline.data.database.dao.CartDao
import com.abdulmateen.pos_offline.data.database.dao.CategoryDao
import com.abdulmateen.pos_offline.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.data.database.dao.UnitDao
import com.abdulmateen.pos_offline.data.database.entities.CartEntity
import com.abdulmateen.pos_offline.data.database.entities.CartItemEntity
import com.abdulmateen.pos_offline.data.database.entities.CategoryEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity
import com.abdulmateen.pos_offline.data.database.entities.ProductEntity
import com.abdulmateen.pos_offline.data.database.entities.UnitEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        UnitEntity::class,
        CartEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1
)
@ConstructedBy(MyAppDatabaseConstructor::class)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun categoryDao(): CategoryDao
    abstract fun unitDao(): UnitDao



    companion object Companion {
        const val DB_NAME = "my_app.db"
    }
}