package com.abdulmateen.pos_offline.common.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdulmateen.pos_offline.feature.main.home.data.database.ProductDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
@ConstructedBy(MyAppDatabaseConstructor::class)
abstract class MyAppDatabase: RoomDatabase() {
    abstract val productDao: ProductDao

    companion object Companion {
        const val DB_NAME = "my_app.db"
    }
}