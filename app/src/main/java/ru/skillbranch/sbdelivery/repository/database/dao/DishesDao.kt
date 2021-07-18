package ru.skillbranch.sbdelivery.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.database.entity.DishPersistEntity

@Dao
interface DishesDao {
    // https://developer.android.com/training/data-storage/room/async-queries
    // Если просим вернуть из базы Single, то это будет One-shot-read, а
    // если - Observable, то получим поток: при изменении данных в базе
    // прилетит обновленный набор по данному Query

    @Query("SELECT * FROM dishes_table")
    fun getAllDishes(): Single<List<DishPersistEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDishes(dishes: List<DishPersistEntity>)

    // https://www.sqlite.org/lang_expr.html
    // https://stackoverflow.com/questions/44184769/android-room-select-query-with-like
    @Query("SELECT * FROM dishes_table WHERE name LIKE '%' || :searchText || '%' ORDER BY name")
    fun findByName(searchText: String): Observable<List<DishPersistEntity>>

    fun findDishesByName(searchText: String): Observable<List<DishEntity>> =
        findByName(searchText.trim()).map { list ->
            list.map {
                DishEntity(it.id, it.category, it.image,"${it.price} ₽", it.name)
            }
        }
}