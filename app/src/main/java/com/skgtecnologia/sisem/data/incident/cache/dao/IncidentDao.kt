package com.skgtecnologia.sisem.data.incident.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.incident.cache.model.IncidentEntity
import com.skgtecnologia.sisem.data.incident.cache.model.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incidentEntity: IncidentEntity): Long

    @Query(
        """
            UPDATE incident
            SET transmi_requests = :transmiRequests
            WHERE id = :incidentId
             """
    )
    suspend fun updateTransmiStatus(incidentId: Long, transmiRequests: List<Map<String, String>>)

    @Query(
        """
            UPDATE incident
            SET patients = :patientEntities
            WHERE id = :incidentId
             """
    )
    suspend fun updatePatientStatus(incidentId: Long, patientEntities: List<PatientEntity>)

    @Query("SELECT * FROM incident WHERE is_active = :isActive ORDER BY id DESC LIMIT 1")
    fun observeActiveIncident(isActive: Boolean = true): Flow<IncidentEntity?>
}
