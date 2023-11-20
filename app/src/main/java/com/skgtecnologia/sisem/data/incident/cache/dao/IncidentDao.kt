package com.skgtecnologia.sisem.data.incident.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.skgtecnologia.sisem.data.incident.cache.model.IncidentEntity

@Dao
interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incidentEntity: IncidentEntity): Long
}
