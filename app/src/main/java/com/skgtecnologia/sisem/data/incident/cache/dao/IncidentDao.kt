package com.skgtecnologia.sisem.data.incident.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.incident.cache.model.IncidentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incidentEntity: IncidentEntity): Long

    @Query("SELECT * FROM incident WHERE is_active = :isActive LIMIT 1")
    fun observeActiveIncident(isActive: Boolean = true): Flow<IncidentEntity?>
}
