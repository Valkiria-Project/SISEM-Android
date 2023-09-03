package com.skgtecnologia.sisem.data.operation.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.operation.cache.model.OperationEntity

@Dao
interface OperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(operationEntity: OperationEntity)

    @Query("SELECT * FROM operation LIMIT 1")
    suspend fun getOperation(): OperationEntity?
}
