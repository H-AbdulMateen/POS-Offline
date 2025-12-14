package com.abdulmateen.pos_offline.common.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.CategoryEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderItemEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.ProductEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.UnitEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        UnitEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1
)
@ConstructedBy(MyAppDatabaseConstructor::class)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao

    companion object Companion {
        const val DB_NAME = "my_app.db"
    }
}