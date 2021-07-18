package ru.skillbranch.sbdelivery.core.notifier

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.database.entity.DishBasketEntity

class BasketNotifierImpl(private val repository: DishesRepositoryContract) : BasketNotifier {
    // Koin кидает ошибку!!!
//    val repository: DishesRepository by inject(DishesRepository::class.java)

    override fun eventSubscribe(): Observable<BasketEvent> =
        // Получаем из БД девайса список блюд из корзины
        repository.getBasketDishes()
            .observeOn(Schedulers.io())
            .toObservable().flatMap { dishes -> Observable.fromIterable(dishes) }
            .map { dish ->
                // трансформируем DishBasketEntity в BasketEvent.AddDish
                val v: BasketEvent = BasketEvent.AddDish(
                    dish.id, dish.title, dish.price
                )
                v
            }.observeOn(AndroidSchedulers.mainThread())

    override fun putDishes(dish: BasketEvent.AddDish) {
        // Сохраняем добавленное в корзину блюдо в БД девайса
        repository.addBasketDish(DishBasketEntity(dish.id, dish.title, dish.price))
    }
}