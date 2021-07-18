package ru.skillbranch.sbdelivery.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import java.util.*

class SearchUseCaseImpl(private val repository: DishesRepositoryContract) : SearchUseCase {

    override fun getDishes(): Single<List<DishEntity>> = repository.getCachedDishes()

    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> =
        // Вариант 1. Получаем из БД все блюда, затем фильтруем по паттерну
        /*repository.getCachedDishes().toObservable()
            .map { dishes ->
                dishes.filter {
                    it.title.lowercase(Locale.ROOT)
                        .contains(searchText.trim().lowercase(Locale.ROOT))
                }
            }*/
        // Вариант 2. Получаем из БД уже отфильтрованные по паттерну блюда
        repository.findDishesByName(searchText)
}