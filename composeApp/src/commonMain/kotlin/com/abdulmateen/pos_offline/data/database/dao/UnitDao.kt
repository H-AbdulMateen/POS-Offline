package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abdulmateen.pos_offline.data.database.entities.UnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Upsert
    suspend fun insertOrUpdate(unit: UnitEntity)
    @Upsert
    suspend fun upsertList(units: List<UnitEntity>)
    @Delete
    suspend fun deleteUnit(unit: UnitEntity)
    @Query("SELECT * FROM units")
    fun getAllUnits(): Flow<List<UnitEntity>>
    @Query("SELECT * FROM units WHERE unitId = :unitId")
    fun getUnitById(unitId: Long): Flow<UnitEntity?>
    @Query("SELECT * FROM units WHERE name LIKE '%' || :name OR symbol LIKE '%' || :symbol")
    fun getUnitByNameOrSymbol(name: String, symbol: String): Flow<UnitEntity?>
    @Query("DELETE FROM units")
    suspend fun clearUnits()





}