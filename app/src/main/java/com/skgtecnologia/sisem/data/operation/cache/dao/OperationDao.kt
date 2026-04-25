package com.skgtecnologia.sisem.data.operation.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.operation.cache.model.OperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(operationEntity: OperationEntity)

    @Query("SELECT * FROM operation LIMIT 1")
    fun observeOperation(): Flow<OperationEntity?>
}
