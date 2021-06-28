package ru.skillbranch.sbdelivery.domain.filter

import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract

class CategoriesFilterUseCase(private val repository: DishesRepositoryContract) : CategoriesFilter {
    override fun categoryFilterDishes(categoryId: String): Single<List<DishEntity>> {
        return TODO()
    }
}