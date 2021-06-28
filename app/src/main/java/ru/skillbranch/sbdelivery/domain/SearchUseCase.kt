package ru.skillbranch.sbdelivery.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity

interface SearchUseCase {
    fun getDishes(): Single<List<DishEntity>>
    fun findDishesByName(searchText: String): Observable<List<DishEntity>>
}