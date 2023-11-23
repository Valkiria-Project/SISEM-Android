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

    // FIXME: Observe the last active Incident only
    @Query("SELECT * FROM incident LIMIT 1")
    fun observeActiveIncident(): Flow<IncidentEntity?>
}
