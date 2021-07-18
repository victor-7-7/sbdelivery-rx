package ru.skillbranch.sbdelivery.domain.filter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError

class CategoriesFilterUseCase(private val repository: DishesRepositoryContract) : CategoriesFilter {

    override fun categoryFilterDishes(categoryId: String): Single<List<DishEntity>> =
        repository.getCachedDishes()
            .observeOn(Schedulers.io())
            .flatMap { dishes ->
                val list = dishes.filter { dish ->
                    // Услови isBlank добавлено для прохождения теста
                    // `when empty categoryId should return full list DishesEntity`
                    if (categoryId.isBlank()) true
                    else dish.categoryId == categoryId
                }
                // Условие isEmpty() с пробросом ошибки добавлено для прохождения теста
                // `when send categoryId should filter empty list throw EmptyDishesError`
                if (list.isEmpty()) Single.error(EmptyDishesError())
                else Single.just(list)
            }.observeOn(AndroidSchedulers.mainThread())
}
