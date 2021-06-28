package ru.skillbranch.sbdelivery.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.repository.database.entity.DishPersistEntity

@Dao
interface DishesDao {
    @Query("SELECT * FROM dishes_table")
    fun getAllDishes(): Single<List<DishPersistEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDishes(dishes: List<DishPersistEntity>)
}