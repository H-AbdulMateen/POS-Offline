package com.abdulmateen.pos_offline.feature.main.home.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.UnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Upsert
    suspend fun insertOrUpdate(unit: UnitEntity)
    @Delete
    suspend fun deleteUnit(unit: UnitEntity)
    @Query("SELECT * FROM units")
    fun getAllUnits(): Flow<List<UnitEntity>>
    @Query("SELECT * FROM units WHERE unitId = :unitId")
    fun getUnitById(unitId: Long): Flow<UnitEntity?>
    @Query("SELECT * FROM units WHERE name = :name OR symbol = :symbol")
    fun getUnitByNameOrSymbol(name: String, symbol: String): UnitEntity?
    @Query("DELETE FROM units")
    suspend fun clearUnits()





}