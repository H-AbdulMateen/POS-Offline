package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "employees")
data class EmployeeEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val employeeId: Long = 0,
    val name: String,
    val position: String,
    val baseSalary: Double,
    val joiningDate: Long = Clock.System.now().toEpochMilliseconds()
)
