package ru.skillbranch.sbdelivery.repository

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.models.Category

interface DishesRepositoryContract {
    fun getDishes(): Single<List<DishEntity>>
    fun getCachedDishes(): Single<List<DishEntity>>
    fun getCategories(): Single<List<Category>>
    fun findDishesByName(searchText: String): Observable<List<DishEntity>>
}