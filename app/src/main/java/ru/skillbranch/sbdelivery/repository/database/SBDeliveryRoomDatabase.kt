package ru.skillbranch.sbdelivery.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.skillbranch.sbdelivery.repository.database.dao.DishesDao
import ru.skillbranch.sbdelivery.repository.database.entity.DishPersistEntity

@Database(entities = [DishPersistEntity::class], version = 1, exportSchema = false)
abstract class SBDeliveryRoomDatabase : RoomDatabase() {
    abstract fun dishesDao(): DishesDao
}

object DatabaseProvider {
    fun newInstance(context: Context): SBDeliveryRoomDatabase =
        Room.databaseBuilder(context.applicationContext, SBDeliveryRoomDatabase::class.java, "sbd_database")
            .build()
}