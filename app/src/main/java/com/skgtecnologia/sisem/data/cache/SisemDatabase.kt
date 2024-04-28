package com.skgtecnologia.sisem.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity
import com.skgtecnologia.sisem.data.cache.converters.CacheConverters
import com.skgtecnologia.sisem.data.incident.cache.dao.IncidentDao
import com.skgtecnologia.sisem.data.incident.cache.model.IncidentEntity
import com.skgtecnologia.sisem.data.notification.cache.dao.NotificationDao
import com.skgtecnologia.sisem.data.notification.cache.model.NotificationEntity
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import com.skgtecnologia.sisem.data.operation.cache.model.OperationEntity

@Database(
    entities = [
        AccessTokenEntity::class,
        IncidentEntity::class,
        NotificationEntity::class,
        OperationEntity::class
    ],
    version = 19
)
@TypeConverters(CacheConverters::class)
abstract class SisemDatabase : RoomDatabase() {

    abstract fun accessTokenDao(): AccessTokenDao

    abstract fun incidentDao(): IncidentDao

    abstract fun notificationDao(): NotificationDao

    abstract fun configDao(): OperationDao
}
