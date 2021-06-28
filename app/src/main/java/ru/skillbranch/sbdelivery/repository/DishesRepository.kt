package ru.skillbranch.sbdelivery.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.database.dao.DishesDao
import ru.skillbranch.sbdelivery.repository.database.entity.DishPersistEntity
import ru.skillbranch.sbdelivery.repository.http.DeliveryApi
import ru.skillbranch.sbdelivery.repository.http.client.DeliveryRetrofitProvider
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import ru.skillbranch.sbdelivery.repository.models.Category
import ru.skillbranch.sbdelivery.repository.models.Dish
import ru.skillbranch.sbdelivery.repository.models.RefreshToken

class DishesRepository(
    private val api: DeliveryApi,
    private val mapper: DishesMapper,
    private val dishesDao: DishesDao
) : DishesRepositoryContract {

    override fun getDishes(): Single<List<DishEntity>> =
        api.refreshToken(RefreshToken(DeliveryRetrofitProvider.REFRESH_TOKEN))
            .flatMap { api.getDishes(0, 1000, "${DeliveryRetrofitProvider.BEARER} ${it.accessToken}") }
            .doOnSuccess { dishes: List<Dish> ->
                val savePersistDishes: List<DishPersistEntity> = mapper.mapDtoToPersist(dishes)
                dishesDao.insertDishes(savePersistDishes)
            }
            .map { mapper.mapDtoToEntity(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun getCachedDishes(): Single<List<DishEntity>> {
        return dishesDao.getAllDishes().map { mapper.mapPersistToEntity(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getCategories(): Single<List<Category>> {
        return api.refreshToken(RefreshToken(DeliveryRetrofitProvider.REFRESH_TOKEN))
            .flatMap { api.getCategories(0, 1000, "${DeliveryRetrofitProvider.BEARER} ${it.accessToken}") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> {
        return TODO("Сделать поиск через дао")
    }
}