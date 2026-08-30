package com.abdulmateen.pos_offline.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.abdulmateen.pos_offline.data.database.dao.CartDao
import com.abdulmateen.pos_offline.data.database.dao.CategoryDao
import com.abdulmateen.pos_offline.data.database.dao.CreditDao
import com.abdulmateen.pos_offline.data.database.dao.DashboardDao
import com.abdulmateen.pos_offline.data.database.dao.EmployeeDao
import com.abdulmateen.pos_offline.data.database.dao.ExpenseDao
import com.abdulmateen.pos_offline.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.data.database.dao.UnitDao
import com.abdulmateen.pos_offline.data.database.entities.CartEntity
import com.abdulmateen.pos_offline.data.database.entities.CartItemEntity
import com.abdulmateen.pos_offline.data.database.entities.CategoryEntity
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.EmployeeEntity
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
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
        OrderItemEntity::class,
        EmployeeEntity::class,
        ExpenseEntity::class,
        CreditEntity::class
    ],
    version = 7
)
@TypeConverters(ExpenseTypeConverter::class)
@ConstructedBy(MyAppDatabaseConstructor::class)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun categoryDao(): CategoryDao
    abstract fun unitDao(): UnitDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun creditDao(): CreditDao



    companion object Companion {
        const val DB_NAME = "my_app.db"

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE expenses ADD COLUMN paidTo TEXT")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("CREATE TABLE IF NOT EXISTS `credits` (`creditId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `customerName` TEXT NOT NULL, `totalAmount` REAL NOT NULL, `paidAmount` REAL NOT NULL, `remainingAmount` REAL NOT NULL, `date` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL)")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE credits ADD COLUMN phoneNumber TEXT")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE orders ADD COLUMN customerPhone TEXT")
            }
        }
    }
}
